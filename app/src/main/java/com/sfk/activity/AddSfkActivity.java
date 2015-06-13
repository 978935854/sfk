package com.sfk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;
import com.sfk.uploadFile.activity.*;
import com.sfk.utils.AddressUtil;
import com.sfk.utils.AsyncHttpClientUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;


public class AddSfkActivity extends Activity {
    private EditText ettitle,etinfo,etscontactway,etaccount,etlasttime,etyourgoods,etothermessage,etspeoplenum;
    private Spinner spstypesp,spprovinces,spsage,spcities,spsex;
    private ImageButton addImage,addSfk;
    private RequestParams params;
    private TextView tpath;
    private Button bchoice;
    private String title,tinfo,scontactway,account,stype,lasttime,yourgoods,othermessage,speoplenum,ssex,provinces,sage,cities,state,stime,sreleasetime;
    public static final int FILE_RESULT_CODE = 1;
    private Intent intent;
    private String author;
    private int size;
    private FinalHttp finalHttp;
    private LinearLayout imagesLaout;
    ImageView imageView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sfk);
        setView();
        setProvinces();
        getSfkinfo();
        choiceImage();
        getAddSfkInfo();
        addSfkOnclick();

    }

    /**
     * 处理省份下拉框的显示
     */
    private void setProvinces(){
        AddressUtil addressUtil=new AddressUtil(AddSfkActivity.this);
        addressUtil.address(spprovinces, spcities);
        //addressUtil.setcities();
    }


    /**
     * 选择图片按钮监听
     */
    private void choiceImage(){
        intent=getIntent();
        if(intent.getExtras()!=null){
            Bundle bundle=intent.getExtras();
            author=bundle.getString("author");
            size=bundle.getInt("size");

            Log.i("author------------------",author+"");
            Log.i("size--------------------",size+"");

            String att=author.substring(author.indexOf("[")+1,author.indexOf("]"));
            String a[]=att.split(",");
            //Log.i("attttttttttttttttttttttt",author);
            for(int i=0;i<size;i++){
                imageView=new ImageView(AddSfkActivity.this);
                File file=new File(a[i].trim());
                ViewGroup.LayoutParams para;
                if(file.exists()){
                    Bitmap bitmap=BitmapFactory.decodeFile(a[i].trim());
                    imageView.setAdjustViewBounds(true);
                    imageView.setMaxHeight(50);
                    imageView.setMaxWidth(50);

                    imageView.setImageBitmap(bitmap);
                    imagesLaout.addView(imageView);
                }
            }
        }

        bchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent();
                intent.setClass(AddSfkActivity.this, com.sfk.uploadFile.activity.MainActivity.class);
                startActivity(intent);
            }

        });
    }


    /**
     * 发布沙发按钮监听，向服务器发送发布的沙发信息
     */
    private void addSfkOnclick(){
        addSfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //发布沙发客异步请求服务器操作
                String url = "http://192.168.1.157:8080/shaFaKe/sfk/SfkAction!addSfk";
                AsyncHttpClientUtil.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(AddSfkActivity.this, "数据返回出错",  Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            //String ms = response.getString("sfk");
                            //Toast.makeText(AddSfkActivity.this, ms,  Toast.LENGTH_SHORT).show();
                            //Log.i("ttttttttttttttttttttt",jsonObject.getString("saddress"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                //上传图片异步请求服务器操作
                String url1 = "http://192.168.1.157:8080/shaFaKe/sfk/SfkAction!upload";
                //http://192.168.1.160:8080/shaFaKe/sfk/SfkAction!upload
                final AjaxParams params = new AjaxParams();
                //String str=uploadBT.getText().toString();
                String att=author.substring(author.indexOf("[")+1,author.indexOf("]"));
                String a[]=att.split(",");
                try {
                    //Log.i("upload---", "upload"+String.valueOf(i));
                    Log.i("upload---", size+"");
                    for(int i=0;i<size;i++){
                        params.put("size",size+"");
                        String name="upload"+(i+1);
                        String fname="fname"+(i+1);
                        Log.i(name+"-------", name);
                        Log.i(name, a[i].trim());
                        params.remove(name);
                        params.remove(fname);
                        params.put(fname, a[i].trim());
                        params.put(name, new File(a[i].trim()));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                finalHttp = new FinalHttp();
                finalHttp.configTimeout(10 * 1000);
                finalHttp.post(url1, params, new AjaxCallBack<String>() {

                    @Override
                    public void onFailure(Throwable t, String strMsg) {
                        //Toast.makeText(AddSfkActivity.this, "上传失败",  Toast.LENGTH_SHORT).show();
                        super.onFailure(t, strMsg);
                    }

                    @Override
                    public void onStart() {
                        //Toast.makeText(AddSfkActivity.this, "开始上传",  Toast.LENGTH_SHORT).show();
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String t) {
                        //Toast.makeText(AddSfkActivity.this, "上传成功",  Toast.LENGTH_SHORT).show();
                        super.onSuccess(t);
                    }

                });
            }
        });
    }

    /**
     * 找到对应ID的组建
     */
    private void setView() {
        ettitle=(EditText)findViewById(R.id.title);
        etinfo=(EditText)findViewById(R.id.info);
        etscontactway=(EditText)findViewById(R.id.scontactway);
        etaccount=(EditText)findViewById(R.id.account);
        etlasttime=(EditText)findViewById(R.id.lasttime);
        etyourgoods=(EditText)findViewById(R.id.yourgoods);
        etothermessage=(EditText)findViewById(R.id.othermessage);
        etyourgoods.setText("客户端向服务器端发送信息！！！");
        etspeoplenum=(EditText)findViewById(R.id.speoplenum);
        spstypesp=(Spinner)findViewById(R.id.stypesp);
        spprovinces=(Spinner)findViewById(R.id.provinces);
        spcities=(Spinner)findViewById(R.id.cities);
        spsage=(Spinner)findViewById(R.id.sage);
        spsex=(Spinner)findViewById(R.id.ssex);
        //addImage=(ImageButton)findViewById(R.id.upImage);
        addSfk=(ImageButton)findViewById(R.id.addsfk);
        //tpath=(TextView)findViewById(R.id.path);
        bchoice=(Button)findViewById(R.id.choice);
        imagesLaout=(LinearLayout)findViewById(R.id.imagesLayout);

    }

    /**
     *  获取填写的要发布沙发客信息
     */
    public void getSfkinfo(){
        title=ettitle.getText().toString();
        tinfo=etinfo.getText().toString();
        scontactway=etscontactway.getText().toString();
        account=etaccount.getText().toString();
        lasttime=etlasttime.getText().toString();
        yourgoods=etyourgoods.getText().toString();
        othermessage=etothermessage.getText().toString();
        speoplenum=etspeoplenum.getText().toString();
        provinces=spprovinces.getSelectedItem().toString();
        cities=spcities.getSelectedItem().toString();
        sage=spsage.getSelectedItem().toString();
        ssex=spsex.getSelectedItem().toString();
        stype=spstypesp.getSelectedItem().toString();

    }


    /**
     * 获取要向服务器发送的数据
     */
    public void getAddSfkInfo() {
        params = new RequestParams();
        params.put("title",title);
        params.put("tinfo",tinfo);
        params.put("scontactway",scontactway);
        params.put("account",account);
        params.put("type",stype);
        params.put("lasttime",lasttime);
        params.put("yourgoods",yourgoods);
        params.put("othermessage",othermessage);
        params.put("speoplenum",speoplenum);
        params.put("provinces",provinces);
        params.put("sage",sage);
        params.put("cities",cities);
        params.put("stype",stype);
        params.put("stime",stime);
        params.put("sreleasetime",sreleasetime);
    }


}
