package com.sfk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2015/5/31.
 */
public class SfInfoActivity extends Activity {
    private TextView sftitle,type,intime,where,sex,age,mannumber,day,selfgoods,otherinfo,contactway,publictime;
    private ScollListview mListView;

    private Handler mhandler=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
            age.setText(sfk.getSage()+"岁");
            day.setText(sfk.getLasttime()+"天");
            selfgoods.setText(sfk.getYourgoods());
            contactway.setText(sfk.getScontactway());
            otherinfo.setText(sfk.getOthermessage());
            publictime.setText(sfk.getSreleasetime());

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sf_info);
        init();
        final int sfID=getIntent().getIntExtra("sfinfoID",1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Sfk sfk=SettextformJsonData(Constant.projectServicePath + "/sfk/Sfkinfo?sfk.sid=" + sfID + "");
                Message message=Message.obtain();
                message.obj=sfk;
                mhandler.sendMessage(message);
            }
        }).start();

        new SfinfoAsyncTask().execute(Constant.projectServicePath + "/sfk/sfphoto?sfk.sid=" + sfID + "");

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
            Bean.setSage(jsonObject.getInt("sage")+"");
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
        mListView=(ScollListview) findViewById(R.id.photolistView);
    }
}
