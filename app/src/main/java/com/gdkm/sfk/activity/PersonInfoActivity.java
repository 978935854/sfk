package com.gdkm.sfk.activity;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.gdkm.sfk.R;
import com.gdkm.sfk.adapter.PersonLVAdapter;
import com.gdkm.sfk.application.LoginApplication;
import com.gdkm.sfk.utils.SQLitePerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonInfoActivity extends Activity {
    private Context context;
    private ListView lvPersonInfo;
    private List<Map<String,Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        list = new ArrayList<Map<String, Object>>();
        Map<String,Object> map;

        map = new HashMap<String, Object>();
        map.put("icon",R.drawable.sousuo);
        map.put("text","查看自己沙发");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon",R.drawable.guanli);
        map.put("text","管理沙发");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon",R.drawable.flat_person_image_4);
        map.put("text","查看我的信息");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon",R.drawable.shenfenyanzheng);
        map.put("text","实名验证");
        list.add(map);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        lvPersonInfo = (ListView) findViewById(R.id.lv_personInfo);
        PersonLVAdapter personLVAdapter = new PersonLVAdapter(context,list);
        lvPersonInfo.setAdapter(personLVAdapter);
        Button btnLoginout = (Button) findViewById(R.id.btn_loginout);
        btnLoginout.setOnClickListener(new MClick());
    }

    class MClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_loginout:
                    loginout();
                    finish();
                break;

                default:
                break;

            }
        }
    }

    private void loginout() {
        LoginApplication loginApplication = (LoginApplication) getApplication();
        SQLitePerson sqLitePerson = null;
        SQLiteDatabase writableDB = null;
        try {
            sqLitePerson = new SQLitePerson(context,"person.db",null,1);
            writableDB = sqLitePerson.getWritableDatabase();
            System.out.println("---loginApplication--"+loginApplication.getOpenId());
            if (loginApplication.getOpenId()==null){
                if (loginApplication.getCemail()==null){
                    writableDB.delete("person","ctelnum = ?",new String[]{loginApplication.getCtelnum()});
                }else{
                    writableDB.delete("person","cemail=?",new String[]{loginApplication.getCemail()});
                }
            }else{
                writableDB.delete("person","openId=?",new String[]{loginApplication.getOpenId()});
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            writableDB.close();
        }

        loginApplication.setCemail(null);
        loginApplication.setCtelnum(null);
        loginApplication.setOpenId(null);
        loginApplication.setHeadPath(null);
        loginApplication.setCid(0);
        loginApplication.setNickName(null);
        MainActivity.initPerson = true;
    }
}
