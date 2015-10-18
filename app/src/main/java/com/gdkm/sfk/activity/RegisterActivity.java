package com.gdkm.sfk.activity;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gdkm.sfk.R;
import com.gdkm.sfk.application.LoginApplication;
import com.gdkm.sfk.pojo.Customer;
import com.gdkm.sfk.pojo.Customercenter;
import com.gdkm.sfk.pojo.Topic;
import com.gdkm.sfk.utils.AsyncActionInvoker;
import com.gdkm.sfk.utils.JsonUtil;
import com.gdkm.sfk.utils.RegexValidateUtil;
import com.gdkm.sfk.utils.SQLitePerson;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RegisterActivity extends Activity {
    private Context context;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etRePassword;
    private Button btnRegister;

    private SQLiteDatabase writableDB,readableDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_register);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        etRePassword = (EditText)findViewById(R.id.et_rePassword);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new MClick());
    }

    private class MClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_register:
                    checkRegisterData();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 校验注册数据
     */
    private boolean checkRegisterData() {
        String strUserName = etUsername.getText().toString();
        String strPassword = etPassword.getText().toString();
        String strRePassword = etRePassword.getText().toString();
        if (null == strUserName || "".equals(strUserName)){
            Toast.makeText(context,"用户名不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }else if (null == strPassword || "".equals(strPassword)){
            Toast.makeText(context,"密码不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }else if (null == strRePassword || "".equals(strRePassword)){
            Toast.makeText(context,"重新输入密码不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if (strPassword.equals(strRePassword)){
                startRegister(strUserName,strPassword);
                return true;
            }else {
                Toast.makeText(context,"密码和第二次输入的不一致！",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     * 开始注册
     * @param strUserName
     * @param strPassword
     */
    private void startRegister(String strUserName, String strPassword) {
        AsyncActionInvoker ai = new AsyncActionInvoker();
        ai.setRequestMethod("POST");
        System.out.println("startRegister---strUserName:"+strUserName);
        if (RegexValidateUtil.checkEmail(strUserName)){
            ai.addField("cemail",strUserName);
        }else if (RegexValidateUtil.checkMobileNumber(strUserName)){
            ai.addField("ctelnum",strUserName);
        }else{
            Toast.makeText(context,"用户名不是邮箱或者手机号码，请重新输入！",Toast.LENGTH_SHORT).show();
        }
        ai.addField("cpassword", strPassword);
        ai.submitMessage("customer/customerAction!checkUserUsabilityAndRegister");
        ai.setOnTextClickListener(new AsyncActionInvoker.OnTextClickListener() {
            @Override
            public void returnMessage(String text) {
                HandleRegisterData(text);
            }
        });
    }

    /**
     * 处理点击注册后返回来的数据
     * @param text
     */
    private void HandleRegisterData(String text) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(text);
            int statu = Integer.valueOf(jsonObject.get("statu")+"");
            if (400==statu){
                Toast.makeText(context,"用户名已存在！",Toast.LENGTH_SHORT).show();
            }else if (200==statu){
                Customer customer = JsonUtil.getBean(jsonObject.get("personal_data") + "", Customer.class);
                Toast.makeText(context,"注册成功！",Toast.LENGTH_SHORT).show();
                initApplication(customer);
                System.out.println("checkUserUsabilityAndRegister:"+customer.getCemail()+","+customer.getCtelnum()+","+customer.getCpassword());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化application数据
     * @param customer
     */
    private void initApplication(Customer customer) {
        LoginApplication application = (LoginApplication) getApplication();
        application.setCid(customer.getCid());
        application.setHeadPath(null);
        if (null == customer.getCemail() || "".equals(customer.getCemail())){
            application.setCtelnum(customer.getCtelnum());
            application.setNickName(customer.getCtelnum());
        } else if (null ==customer.getCtelnum() || "".equals(customer.getCtelnum())){
            application.setCemail(customer.getCemail());
            application.setNickName(customer.getCemail());
        }
        saveMessageToDB2(application);
        MainActivity.initPerson=true;
        finish();
    }

    /**
     * 保存普通登录等信息到数据库
     * @param loginApplication    登陆后返回来的数据
     */
    private void saveMessageToDB2(LoginApplication loginApplication) {
        SQLitePerson sQLitePerson = new SQLitePerson(context, "person.db", null, 1);
        readableDB = sQLitePerson.getReadableDatabase();
        Cursor cursor = null;
        try {//假如数据库没有该用户登录信息，就录入，否则跳过
            cursor = readableDB.rawQuery("select * from person where ctelnum=? or cemail=?",
                    new String[]{loginApplication.getCtelnum()+"",loginApplication.getCemail()+""});
            if (cursor.isAfterLast()) {
                writableDB = sQLitePerson.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("nickname", loginApplication.getNickName());
                contentValues.put("figureurl_2", loginApplication.getHeadPath());
                contentValues.put("ctelnum",loginApplication.getCtelnum());
                contentValues.put("cemail",loginApplication.getCemail());
                writableDB.insert("person", "personId", contentValues);
                writableDB.close();
            }
//            cursor.close();
//            readableDB.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor!=null || readableDB!=null){
                cursor.close();
                readableDB.close();
            }
        }
    }

}
