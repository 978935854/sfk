package com.gdkm.sfk.asynwork;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.activity.MainActivity;
import com.gdkm.sfk.activity.NetUtils;
import com.gdkm.sfk.application.LoginApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 5/17/15.
 */
public class RunPostToLoginAsyncTask extends AsyncTask<String, String, String>{
    private Context context;
    private EditText et_username, et_password;
    private TextView tv_info;
    private Application application;

    public RunPostToLoginAsyncTask(Context context, EditText et_username, EditText et_password, TextView tv_info, Application application) {
        this.context = context;
        this.et_username = et_username;
        this.et_password = et_password;
        this.tv_info = tv_info;
        this.application = application;
    }

    @Override
    protected String doInBackground(String[] params) {

        String result = NetUtils.loginOfPost(et_username.getText().toString(),
                et_password.getText().toString(),
                Constant.projectServicePath + "customer/login!login");


        return result;
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject object = jsonObject.getJSONObject("personal_data");
            int num = jsonObject.getInt("status");
            switch (num) {
                case 200:
                    tv_info.setText("登录成功！");
                    SharedPreferences spf = context.getSharedPreferences("LOGIN_STATUS", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("username", object.getString("cemail"));
                    editor.commit();
                    LoginApplication personalApp = (LoginApplication) application;
                    personalApp.setCid(object.getInt("cid"));
                    personalApp.setCemail(object.getString("cemail"));
                    personalApp.setCtelnum(object.getString("ctelnum"));
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    break;
                case 400:
                    tv_info.setText("登录失败，帐号密码错误！");
                    break;
                case 404:
                    tv_info.setText("服务器没有响应！");
                    break;
                default:
                    tv_info.setText("不能连接服务器！");
                    break;
            }
            Log.i("result", num + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_info.setVisibility(View.VISIBLE);
        new DismissViewAsynTask(tv_info).execute();
    }
}
