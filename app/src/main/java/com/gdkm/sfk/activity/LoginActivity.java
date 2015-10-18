package com.gdkm.sfk.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdkm.sfk.R;
import com.gdkm.sfk.application.LoginApplication;
import com.gdkm.sfk.listener.BaseUiListener;
import com.gdkm.sfk.listener.UserInfoIUiListener;
import com.gdkm.sfk.pojo.Customercenter;
import com.gdkm.sfk.pojo.Topic;
import com.gdkm.sfk.utils.AsyncActionInvoker;
import com.gdkm.sfk.utils.JsonUtil;
import com.gdkm.sfk.utils.RegexValidateUtil;
import com.gdkm.sfk.utils.SQLitePerson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class LoginActivity extends Activity implements View.OnClickListener {
    private Context context;
    private EditText et_username, et_password;
    private Button btn_login,btnRegister;
    private ImageButton imgBtnLoginQQ,imgBtnHead;
    private ImageButton imgBtnValidata;
    private TextView tvInfo;
    private RelativeLayout rlyLogin;
    private RelativeLayout rlyLoadingprogress;

    private Tencent mTencent;
    private String APP_ID = "1104743857";//腾讯QQ的APPID，用于登陆
    private IUiListener tencentListener;
    private UserInfo mInfo;
    private String headPath;
    private String openId = "";//QQ用户的唯一ID

    private SQLiteDatabase writableDB,readableDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        context = this;
        initViews();
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        rlyLogin = (RelativeLayout) findViewById(R.id.rly_login);
        rlyLoadingprogress = (RelativeLayout) findViewById(R.id.rly_loadingprogress);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        btn_login.setOnClickListener(this);
        imgBtnLoginQQ = (ImageButton) findViewById(R.id.imgBtn_loginQQ);
        imgBtnLoginQQ.setOnClickListener(this);
        imgBtnHead = (ImageButton) findViewById(R.id.imgBtn_head);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                Intent intent = new Intent(context, RegisterActivity.class);
                context.startActivity(intent);
                finish();
                break;
            case R.id.imgBtn_loginQQ:
                rlyLoadingprogress.setVisibility(View.VISIBLE);//显示进度条
                initTencent();
                loginQQ();
                break;
            default:
                break;
        }
    }


    /**
     * 初始化qq信息
     * @param jsonObject
     */
    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    /**
     * 登陆qq方法
     */
    private void loginQQ() {
        tencentListener = new BaseUiListener(context){
            @Override
            protected void doComplete(JSONObject values) {
                initOpenidAndToken(values);
                updateUserInfo(values);
            }
        };

        mTencent.login(this, "all", tencentListener);

    }

    /*QQ登录成功了，我们通过这个类UserInfo，获取一些QQ的基本信息，比如昵称，头像什么的*/
    private void updateUserInfo(JSONObject values){
        UserInfoIUiListener userInfoIUiListener = new UserInfoIUiListener(){
            @Override
            public void doComplete(final JSONObject jsonObject) {
        try {
            System.out.println("-----jsonObject---" + jsonObject);
            saveMessageToDB(jsonObject);
            saveMessageToApplication(jsonObject);
            MainActivity.initPerson = true;//允许改变Person_Fragment界面
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
            }
        };
        QQToken qqToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(context,qqToken);
        userInfo.getUserInfo(userInfoIUiListener);
    }

    /**
     * 保存用户信息到application
     * @param jsonObject
     * @throws JSONException
     */
    private void saveMessageToApplication(JSONObject jsonObject) throws JSONException {
        LoginApplication loginApplication = (LoginApplication) getApplication();
        String nickName =  jsonObject.get("nickname").toString();
        headPath = jsonObject.get("figureurl_2").toString();
        loginApplication.setNickName(nickName);
        loginApplication.setOpenId(openId);
        loginApplication.setHeadPath(headPath);
        System.out.println("----saveMessage---" + nickName);
    }

    /**
     * 保存QQ等信息到数据库
     * @param jsonObject    QQ登陆后返回来的jsonObject数据
     */
    private void saveMessageToDB(JSONObject jsonObject) throws JSONException {
        SQLitePerson sQLitePerson = new SQLitePerson(context, "person.db", null, 1);
        readableDB = sQLitePerson.getReadableDatabase();
        Cursor cursor = null;
        try {//假如数据库没有该用户登录信息，就录入，否则跳过
            cursor = readableDB.rawQuery("select * from person where openId=?", new String[]{openId});
            if (cursor.isAfterLast()) {
                writableDB = sQLitePerson.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("nickname", jsonObject.get("nickname").toString());
                contentValues.put("openId", openId);
                contentValues.put("sex", jsonObject.get("gender").toString());
                contentValues.put("province", jsonObject.get("province").toString());
                contentValues.put("city", jsonObject.get("city").toString());
                contentValues.put("figureurl_2", jsonObject.get("figureurl_2").toString());
                writableDB.insert("person", "personId", contentValues);
                writableDB.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null || readableDB!=null){
                cursor.close();
                readableDB.close();
            }
        }
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

    /**
     * 初始化腾讯sdk
     */
    private void initTencent() {
        mTencent = Tencent.createInstance(APP_ID,context);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==4){
                Bitmap bitmap = (Bitmap) msg.obj;
                imgBtnHead.setImageBitmap(bitmap);
            }else{
                String text = (String) msg.obj;
                initLoginMessage(text);

            }

        }
    };

    private void initLoginMessage(String text) {
        String status = null;
        Customercenter customercenter = null;
        try {
            JSONObject jsonObject = new JSONObject(text);
            status = jsonObject.get("status").toString();
            JSONObject personal_data = jsonObject.getJSONObject("personal_data");
//            customercenter = JsonUtil.convertToObj(personal_data, Customercenter.class);
            customercenter = JsonUtil.getBean(personal_data+"", Customercenter.class);
            System.out.println("----customercenter---"+customercenter.getCid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status.equals("400")){
            tvInfo.setText("账号或者密码错误，请重新输入登陆！");
        }else if (status.equals("200")){
            tvInfo.setText("");
            Toast.makeText(context,"登陆成功！",Toast.LENGTH_SHORT).show();
            LoginApplication application = (LoginApplication) getApplication();
            application.setCid(customercenter.getCid());
            application.setHeadPath(customercenter.getCcheadphoto());
            System.out.println("---getCcusername---" + customercenter.getCcusername());
            if (customercenter.getCcusername()==null || customercenter.getCcusername().equals("")){
                application.setNickName(et_username.getText().toString());
            }else {
                application.setNickName(customercenter.getCcusername());
            }
            if (RegexValidateUtil.checkEmail(et_username.getText().toString())){
                application.setCemail(et_username.getText().toString());
            }else if (RegexValidateUtil.checkMobileNumber(et_username.getText().toString())){
                application.setCtelnum(et_username.getText().toString());
            }
            saveMessageToDB2(application);
            MainActivity.initPerson=true;
            finish();
        }
    }

    /**
     * 登陆方法
     */
    private void login(){
        AsyncActionInvoker ai = new AsyncActionInvoker();
        ai.setRequestMethod("POST");
        if (RegexValidateUtil.checkEmail(et_username.getText().toString())){
            ai.addField("customer.cemail", et_username.getText().toString());
        }else if (RegexValidateUtil.checkMobileNumber(et_username.getText().toString())){
            ai.addField("customer.ctelnum", et_username.getText().toString());
        }else{
            return;
        }
        ai.addField("customer.cpassword", et_password.getText().toString());
        ai.submitMessage("customer/customerAction!login");
        ai.setOnTextClickListener(new AsyncActionInvoker.OnTextClickListener() {
            @Override
            public void returnMessage(String text) {
                Message msg = new Message();
                msg.obj = text;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 在某些低端机上调用登录后，由于内存紧张导致APP被系统回收，登录成功后无法成功回传数据。解决办法如下
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("login11", "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        mTencent.onActivityResultData(requestCode,resultCode,data,tencentListener);
        if(requestCode == Constants.REQUEST_API) {
            if(resultCode == Constants.RESULT_LOGIN) {
                Tencent.handleResultData(data, tencentListener);
            }
        } else if (requestCode == Constants.REQUEST_APPBAR) { //app内应用吧登录
            if (resultCode == Constants.RESULT_LOGIN) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
