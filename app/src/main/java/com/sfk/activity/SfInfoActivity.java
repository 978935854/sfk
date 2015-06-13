package com.sfk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sfk.pojo.Sfk;

/**
 * Created by Administrator on 2015/5/31.
 */
public class SfInfoActivity extends Activity {
    private TextView title,type,intime,where,sex,age,mannumber,day,selfgoods,otherinfo,contactway,publictime;
    private Sfk sfk = new Sfk();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sf_info);
        Intent intent = getIntent();
        sfk = (Sfk) intent.getSerializableExtra("sfk");
        initView(); //初始化界面
        intData();  //初始化数据
    }
    //初始化界面
    private void initView() {
        title = (TextView)findViewById(R.id.title);
        type = (TextView)findViewById(R.id.type);
        intime = (TextView)findViewById(R.id.intime);
        where = (TextView)findViewById(R.id.where);
        sex = (TextView)findViewById(R.id.sex);
        age = (TextView)findViewById(R.id.age);
        day = (TextView)findViewById(R.id.day);
        mannumber = (TextView)findViewById(R.id.mannumber);
        selfgoods = (TextView)findViewById(R.id.selfgoods);
        otherinfo = (TextView)findViewById(R.id.otherinfo);
        contactway = (TextView)findViewById(R.id.contactway);
        publictime = (TextView)findViewById(R.id.publictime);
    }

    //初始化数据
    private void intData() {
        title.setText(sfk.getStitle());
        age.setText(sfk.getSage()+"");
        day.setText(sfk.getLasttime()+"");
        selfgoods.setText(sfk.getYourgoods());
        otherinfo.setText(sfk.getOthermessage());
        contactway.setText(sfk.getScontactway());
        publictime.setText(sfk.getSreleasetime());
        intime.setText(sfk.getStime());
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
        }

        switch (sfk.getSsex()){
            case 0:
                sex.setText("所有");
                break;
            case 1:
                sex.setText("女");
                break;
            case 2:
                sex.setText("男");
                break;
        }

        switch (sfk.getSpeoplenum()){
            case 0:
                mannumber.setText("所有");
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
        }
    }

}
