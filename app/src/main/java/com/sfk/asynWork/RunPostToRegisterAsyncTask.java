package com.sfk.asynWork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sfk.Constant.Constant;
import com.sfk.activity.MainActivity;
import com.sfk.activity.NetUtils;
import com.sfk.application.LoginAplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 5/17/15.
 */
public class RunPostToRegisterAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    private EditText et_username, et_password;
    private TextView tv_info;

    public RunPostToRegisterAsyncTask(Context context, EditText et_username,
                                      EditText et_password, TextView tv_info) {
        this.context = context;
        this.et_username = et_username;
        this.et_password = et_password;
        this.tv_info = tv_info;
    }

    @Override
    protected String doInBackground(String[] params) {
        String result = NetUtils.loginOfPost(et_username.getText().toString(),
                et_password.getText().toString(),
                Constant.projectServicePath + "customer/register!checkUserUsabilityAndRegister");
        return result;
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject object = jsonObject.getJSONObject("personal_data");
            Log.i("gggg", object.toString());
            int num = jsonObject.getInt("status");
            switch (num) {
                case 200:
                    tv_info.setText("注册成功！");
                    SharedPreferences spf = context.getSharedPreferences("LOGIN_STATUS", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("username", object.getString("cemail"));
                    editor.commit();
                    LoginAplication loginAplication = new LoginAplication();
                    loginAplication.setCid(object.getInt("cid"));
                    loginAplication.setCtelnum(object.getString("ctelnum"));
                    loginAplication.setCemail(object.getString("cemail"));
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    break;
                case 400:
                    tv_info.setText("注册失败，请填写正确的用户！");
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


