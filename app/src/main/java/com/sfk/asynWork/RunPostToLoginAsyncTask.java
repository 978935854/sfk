package com.sfk.asynWork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sfk.activity.MainActivity;
import com.sfk.activity.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 5/17/15.
 */
public class RunPostToLoginAsyncTask extends AsyncTask{
    private Context context;
    private EditText et_username, et_password;
    private TextView tv_info;

    public RunPostToLoginAsyncTask(Context context, EditText et_username, EditText et_password, TextView tv_info) {
        this.context = context;
        this.et_username = et_username;
        this.et_password = et_password;
        this.tv_info = tv_info;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String result = NetUtils.loginOfPost(et_username.getText().toString(),
                et_password.getText().toString(),
                "http://192.168.1.112:8080/shaFaKe/customer/login!login");


        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        String info = (String) o;
        try {
            JSONObject jsonObject = new JSONObject(info);
            int num = jsonObject.getInt("info");
            switch (num) {
                case 200:
                    tv_info.setText("登录成功！");
                    SharedPreferences spf = context.getSharedPreferences("LOGIN_STATUS", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("username", jsonObject.getString("username"));
                    editor.commit();
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
