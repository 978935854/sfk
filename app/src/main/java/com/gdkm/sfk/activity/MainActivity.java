package com.gdkm.sfk.activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gdkm.sfk.R;
import com.gdkm.sfk.application.LoginApplication;
import com.gdkm.sfk.fragment.BbsFragment;
import com.gdkm.sfk.fragment.PanicFragment;
import com.gdkm.sfk.fragment.PersonFragment;
import com.gdkm.sfk.pojo.Sfk;
import com.gdkm.sfk.service.BBSservice;
import com.gdkm.sfk.utils.SQLitePerson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private Context context;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton[] radioButtons;
    private List<Sfk> seekSFTopicList = new ArrayList<Sfk>();
    private int panic_action=0;
    private int backDotNumber=0;
    private long startTime=0,lastTime=0;
    BBSservice bbSservice=new BBSservice(this);

    public static boolean initPerson = false;
    private SQLiteDatabase readableDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
        context = this;

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
        checkPersonInfo();
	}

    /**
     * 检查用户信息
     */
    private void checkPersonInfo() {
        SQLitePerson sQLitePerson = new SQLitePerson(context,"person.db",null,1);
        readableDB = sQLitePerson.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = readableDB.rawQuery("select * from person", new String[]{});
            initPersonInfo(cursor);
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
     * 初始化用户数据
     * @param cursor
     */
    private void initPersonInfo(Cursor cursor) {
        int cid = 0;
        String nickName = null;
        String openId = null;
        String headPath = null;
        String ctelnum = null;
        String cemail = null;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            cid = cursor.getInt(cursor.getColumnIndex("personId"));
            nickName = cursor.getString(cursor.getColumnIndex("nickname"));
            openId = cursor.getString(cursor.getColumnIndex("openId"));
            headPath = cursor.getString(cursor.getColumnIndex("figureurl_2"));
            ctelnum = cursor.getString(cursor.getColumnIndex("ctelnum"));
            cemail = cursor.getString(cursor.getColumnIndex("cemail"));
            cursor.moveToNext();
        }
        System.out.println("----mainActivity---"+nickName);
        if (nickName!=null){
            LoginApplication loginApplication = (LoginApplication) getApplication();
            loginApplication.setCid(cid);
            loginApplication.setOpenId(openId);
            loginApplication.setNickName(nickName);
            loginApplication.setHeadPath(headPath);
            loginApplication.setCemail(cemail);
            loginApplication.setCtelnum(ctelnum);
            PersonFragment personFragment = (PersonFragment) fragments[3];
            personFragment.setPersonMessage(loginApplication);
        }
    }


    //fragment管理
    private void setFragmentIndicator(){
        radioButtons = new RadioButton[4];
        radioButtons[0] = (RadioButton) findViewById(R.id.main_radioButton);
        radioButtons[1] = (RadioButton) findViewById(R.id.panic_radioButton);
        radioButtons[2] = (RadioButton) findViewById(R.id.bbs_radioButton);
        radioButtons[3] = (RadioButton) findViewById(R.id.person_radioButton);
        RadioGroup main_RadioGroup = (RadioGroup) findViewById(R.id.main_RadioGroup);
        main_RadioGroup.setOnCheckedChangeListener(new MCheckedChangeListener());
    }

    /**
     * 主页底部radioGroup监听事件
     */
    class MCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            fragmentTransaction = fragmentManager.beginTransaction()
                    .hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3]);
            switch (checkedId){
                case R.id.main_radioButton:     //首页
                    fragmentTransaction.show(fragments[0]).commit();
                    break;
                case R.id.panic_radioButton:    //抢沙发
                    PanicFragment panic_fragments = (PanicFragment) fragments[1];
                    if(panic_action==0){    //判断是否之前已加载该fragment数据，如没有就加载
                        panic_fragments.loadFirstData();
                        panic_action=1;
                    }
                    fragmentTransaction.show(fragments[1]).commit();
                    break;
                case R.id.bbs_radioButton:      //社区
//                    if(bbSservice.alreadUpdate) {
//                        try {
//                            bbSservice.bbsService();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }//社区
                    BbsFragment bbsFragment = (BbsFragment) fragments[2];
                    bbsFragment.initData();
                    fragmentTransaction.show(fragments[2]).commit();
                    break;
                case R.id.person_radioButton:   //个人管理
                    fragmentTransaction.show(fragments[3]).commit();
                    break;
                default:
                    break;
            }
        }
    }

    //当点击返回会提示是否退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
//            dialog();
            if (backDotNumber==0){
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                startTime = System.currentTimeMillis();
                backDotNumber++;
            }else{
                lastTime = System.currentTimeMillis();
//                Log.i("startTime,lastTime,jieguo",startTime+","+lastTime+","+(lastTime-startTime));
                if (lastTime-startTime>=1500){
                    startTime=0;
                    lastTime=0;
                    backDotNumber=0;
                }else{
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            return true;
        }
        event.getAction();
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("---onResume--"+initPerson);
        if (initPerson){
            LoginApplication loginApplication = (LoginApplication) getApplication();
            PersonFragment personFragment = (PersonFragment) fragments[3];
            System.out.println("---nickName:"+loginApplication.getNickName());
            if (loginApplication.getNickName()!=null){
                personFragment.setPersonMessage(loginApplication);
            }else{
                personFragment.setPersonMessage2();
            }
            initPerson = false;
        }
    }
}
