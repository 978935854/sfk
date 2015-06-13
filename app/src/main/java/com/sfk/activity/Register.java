package com.sfk.activity;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sfk.asynWork.RunPostToLoginAsyncTask;
import com.sfk.asynWork.RunPostToRegisterAsyncTask;


public class Register extends Activity implements View.OnClickListener {
    private EditText et_username, et_password;
    private TextView tv_forgetPassword;
    private Button btn_login, btn_register;
    private TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);

        tv_forgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                new RunPostToLoginAsyncTask(this, et_username, et_password, tv_info,this.getApplication()).execute();
                break;
            case R.id.btn_register:
                new RunPostToRegisterAsyncTask(this, et_username, et_password, tv_info, this.getApplication()).execute();
                break;
            default:
                break;
        }
    }



}
