package com.sfk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;
import com.sfk.utils.AddressUtil;
import com.sfk.utils.AsyncHttpClientUtil;

import org.apache.http.Header;
import org.json.JSONObject;


public class AddSfkActivity extends Activity {
    private EditText ettitle,etinfo,etscontactway,etaccount,etlasttime,etyourgoods,etothermessage,etspeoplenum;
    private Spinner spstypesp,spprovinces,spsage,spcities,spsex;
    private ImageButton addImage,addSfk;
    private RequestParams params;
    private TextView tpath;
    private Button bchoice;
    private String title,tinfo,scontactway,account,stype,lasttime,yourgoods,othermessage,speoplenum,ssex,provinces,sage,cities,state,stime,sreleasetime;
    public static final int FILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sfk);
        setView();
        setProvinces();
        getSfkinfo();
        getAddSfkInfo();
        //图片上传
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(FILE_RESULT_CODE == requestCode){
            Bundle bundle = null;
            if(data!=null&&(bundle=data.getExtras())!=null){
                tpath.setText("选择文件夹为："+bundle.getString("file"));
            }
        }
    }


    /**
     * 发布沙发按钮监听，向服务器发送发布的沙发信息
     */
    private void addSfkOnclick(){
        addSfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.1.160:8080/shaFaKe/sfk/SfkAction!addSfk";
                AsyncHttpClientUtil.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.i("errorrrrrrrrrrrrr", "数据返回出错！");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONObject jsonObject = response.getJSONObject("sfk");
                            Log.i("ttttttttttttttttttttt", jsonObject.toString());
                            //Log.i("ttttttttttttttttttttt",jsonObject.getString("saddress"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        addImage=(ImageButton)findViewById(R.id.upImage);
        addSfk=(ImageButton)findViewById(R.id.addsfk);
        tpath=(TextView)findViewById(R.id.path);
        bchoice=(Button)findViewById(R.id.choice);
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
