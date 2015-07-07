package com.sfk.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sfk.Constant.Constant;
import com.sfk.adapter.SfinfoAdapter;
import com.sfk.pojo.Photo;
import com.sfk.pojo.Sfk;
import com.sfk.utils.ScollListview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/31.
 */
public class SfInfoActivity extends Activity {
    private TextView sftitle,qskinfo,type,intime,where,sex,age,mannumber,day,selfgoods,otherinfo,contactway,publictime;
    private ScollListview mListView;
    private Button qskbutton;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private LinearLayout loading_ProgressBar,infolayout;

    private Handler mhandler=new Handler() {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Sfk sfk= (Sfk) msg.obj;

                sftitle.setText(sfk.getStitle());
                where.setText(sfk.getSaddress());

                switch (sfk.getStype()){
                    case 0:
                        type.setText("其他");
                        break;
                    case 1:
                        type.setText("地铺");
                        break;
                    case 2:
                        type.setText("客房");
                        break;
                    case 3:
                        type.setText("沙发");
                        break;
                    default:
                        type.setText("沙发");
                        break;
                }


                switch (sfk.getSpeoplenum()){
                    case 0:
                        mannumber.setText("所有人");
                        break;
                    case 1:
                        mannumber.setText("1人");
                        break;
                    case 2:
                        mannumber.setText("2人");
                        break;
                    case 3:
                        mannumber.setText("3人");
                        break;
                    case 4:
                        mannumber.setText("3人以上");
                        break;
                    default:
                        mannumber.setText("所以人");
                        break;
                }

                switch (sfk.getSsex()){
                    case 0:
                        sex.setText("男女不限");
                        break;
                    case 1:
                        sex.setText("只接待女");
                        break;
                    case 2:
                        sex.setText("只接待男");
                        break;
                    default:
                        sex.setText("男女不限");
                        break;
                }
                intime.setText(sfk.getStime());
                age.setText(sfk.getSage());
                day.setText(sfk.getLasttime()+"天");
                selfgoods.setText(sfk.getYourgoods());
                contactway.setText(sfk.getScontactway());
                otherinfo.setText(sfk.getOthermessage());
                publictime.setText(sfk.getSreleasetime());

                //滚动条渐变模糊度始终
                AlphaAnimation aa = new AlphaAnimation(1.0f,0f);
                //渐变时间
                aa.setDuration(500);
                aa.setRepeatCount(0);
                loading_ProgressBar.startAnimation(aa);

                loading_ProgressBar.setVisibility(View.GONE);//加载完listview关闭数据圆形滚动条
                infolayout.setAlpha(1);


            }else if(msg.what==2){
                int count =msg.arg1;
                if(count>10) {
                    qskbutton.setBackgroundColor(Color.GRAY);
                    qskbutton.setEnabled(false);
                    qskinfo.setText("抱歉,你来迟了,沙发被抢走了！");
                }
            }else if(msg.what==3){

                progressDialog.dismiss();
                //  Toast.makeText(getApplication(), "抢沙发成功", Toast.LENGTH_LONG).show();
                builder = new AlertDialog.Builder(SfInfoActivity.this);
                if(msg.arg1==0) {
                    builder.setMessage("抢沙发成功，请耐心等待沙主的回复。");
                }else if(msg.arg1==1){
                    builder.setMessage("该沙发已被抢走了,去看看其他沙发吧。");
                }else{
                    builder.setMessage("你已经抢过该沙发。");
                }
                builder.setTitle("提示");

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                builder.create().show();
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sf_info);
        init();

        loading_ProgressBar.setVisibility(View.VISIBLE);//添加圆形滚动条
        final int sfID=getIntent().getIntExtra("sfinfoID",1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Sfk sfk=SettextformJsonData(Constant.projectServicePath + "/sfk/Sfkinfo?sfk.sid=" + sfID + "");
                Message message=Message.obtain();
                message.what=1;
                message.obj=sfk;
                mhandler.sendMessage(message);

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int count=getQskcount(Constant.projectServicePath + "/purchasing/queryPurchasingCount?purchasing.sid=" + sfID + "");
                Message message=Message.obtain();
                message.what=2;
                message.arg1=count;
                mhandler.sendMessage(message);
            }
        }).start();
        new SfinfoAsyncTask().execute(Constant.projectServicePath + "/sfk/sfphoto?sfk.sid=" + sfID + "");

        qskbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = ProgressDialog.show(SfInfoActivity.this, "提示", "请稍后……",false,true);
                  /* 开启一个新线程，在新线程里执行耗时的方法 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //抢沙发
                        int status=getQskcount(Constant.projectServicePath + "/purchasing/addtchasingcount?purchasing.cid=1&purchasing.sid=" + sfID + "");

                        //spandTimeMethod();// 耗时的方法
                        Message message=Message.obtain();
                        message.what=3;
                        message.arg1=status;
                        mhandler.sendMessage(message);
                        //  Toast.makeText(getApplication(), "抢沙发成功", Toast.LENGTH_LONG).show();
                    }

                }).start();
            }
        });
    }

    class SfinfoAsyncTask extends AsyncTask<String,Void,List<Photo>> {


        @Override
        protected List<Photo> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            super.onPostExecute(photos);
            SfinfoAdapter adapter=new SfinfoAdapter(SfInfoActivity.this,photos,mListView);
            mListView.setAdapter(adapter);
        }
    }
    private int getQskcount(String url) {
        String jsonString = null;
        int count = 0;
        try {
            jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject = new JSONObject(jsonString);
            count = jsonObject.getInt("qsfcount");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return count;
    }

    private List<Photo> getJsonData(String url) {
        List<Photo> beanlist=new ArrayList<Photo>();

        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            Photo newsBean;
            Log.i("jsonString",jsonString);
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("sfphotolist");
            for(int i=0; i<jsonArray.length(); i++){
                jsonObject =jsonArray.getJSONObject(i);
                newsBean = new Photo();
                newsBean.setPath(Constant.projectServicePath+jsonObject.getString("path"));
                beanlist.add(newsBean);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beanlist;
    }
    private Sfk SettextformJsonData(String url) {
        Sfk Bean = new Sfk();
        String jsonString = null;
        try {
            jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject = new JSONObject(jsonString);

            Bean.setStitle(jsonObject.getString("stitle"));
            Bean.setSid(jsonObject.getInt("sid"));
            Bean.setCcid(jsonObject.getInt("ccid"));
            Bean.setTid(jsonObject.getInt("tid"));
            Bean.setSaddress(jsonObject.getString("saddress"));
            Bean.setStype(jsonObject.getInt("stype"));
            Bean.setStime(jsonObject.getString("stime"));
            Bean.setSpeoplenum(jsonObject.getInt("speoplenum"));
            Bean.setSsex(jsonObject.getInt("ssex"));
            Bean.setSage(jsonObject.getString("sage"));
            Bean.setLasttime(jsonObject.getInt("lasttime"));
            Bean.setYourgoods(jsonObject.getString("yourgoods"));
            Bean.setScontactway(jsonObject.getString("scontactway"));
            Bean.setOthermessage(jsonObject.getString("othermessage"));
            Bean.setSreleasetime(jsonObject.getString("sreleasetime"));
            Bean.setSdotnumber(jsonObject.getInt("sdotnumber"));
            Bean.setSstate(jsonObject.getInt("sstate"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Bean;
    }

    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";
        String line="";
        try {
            isr=new InputStreamReader(is,"UTF-8");
            BufferedReader br=new BufferedReader(isr);
            while((line = br.readLine())!=null){
                result+=line;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private  void init(){
        infolayout=(LinearLayout)findViewById(R.id.InfoLayout);
        loading_ProgressBar=(LinearLayout)findViewById(R.id.loading_ProgressBar);
        sftitle=(TextView)findViewById(R.id.sftitle);
        type=(TextView)findViewById(R.id.type);
        intime=(TextView)findViewById(R.id.intime);
        where=(TextView)findViewById(R.id.where);
        sex=(TextView)findViewById(R.id.sex);
        age=(TextView)findViewById(R.id.age);
        mannumber=(TextView)findViewById(R.id.mannumber);
        day=(TextView)findViewById(R.id.day);
        selfgoods=(TextView)findViewById(R.id.selfgoods);
        otherinfo=(TextView)findViewById(R.id.otherinfo);
        contactway=(TextView)findViewById(R.id.contactway);
        publictime=(TextView)findViewById(R.id.publictime);
        qskinfo=(TextView)findViewById(R.id.qskinfo);
        mListView=(ScollListview) findViewById(R.id.photolistView);
        qskbutton=(Button)findViewById(R.id.qsfbutton);
    }
    private void spandTimeMethod() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
