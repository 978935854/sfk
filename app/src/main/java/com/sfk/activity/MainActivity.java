package com.sfk.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends ActionBarActivity {
    private Fragment[] fragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton[] radioButtons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
        SharedPreferences spf = getSharedPreferences("LOGIN_STATUS", 0);
        if (spf.getString("username", null) == null) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
        fragments = new Fragment[4];
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragments[0] = fragmentManager.findFragmentById(R.id.main_fragment);
        fragments[1] = fragmentManager.findFragmentById(R.id.panic_fragment);
        fragments[2] = fragmentManager.findFragmentById(R.id.bbs_fragment);
        fragments[3] = fragmentManager.findFragmentById(R.id.person_fragment);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3]);
        fragmentTransaction.show(fragments[0]).commit();
        setFragmentIndicator();
	}

    //fragment管理
    private void setFragmentIndicator(){
        radioButtons = new RadioButton[4];
        radioButtons[0] = (RadioButton) findViewById(R.id.main_radioButton);
        radioButtons[1] = (RadioButton) findViewById(R.id.panic_radioButton);
        radioButtons[2] = (RadioButton) findViewById(R.id.bbs_radioButton);
        radioButtons[3] = (RadioButton) findViewById(R.id.person_radioButton);
        RadioGroup main_RadioGroup = (RadioGroup) findViewById(R.id.main_RadioGroup);
        main_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction()
                        .hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3]);
                switch (checkedId){
                    case R.id.main_radioButton:     //首页
                        fragmentTransaction.show(fragments[0]).commit();
                        break;
                    case R.id.panic_radioButton:    //抢沙发
                        fragmentTransaction.show(fragments[1]).commit();
                        break;
                    case R.id.bbs_radioButton:      //社区
                        fragmentTransaction.show(fragments[2]).commit();
                        break;
                    case R.id.person_radioButton:   //个人管理
                        fragmentTransaction.show(fragments[3]).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
