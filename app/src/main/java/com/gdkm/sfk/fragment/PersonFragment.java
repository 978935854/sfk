package com.gdkm.sfk.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gdkm.sfk.R;
import com.gdkm.sfk.activity.LoginActivity;
import com.gdkm.sfk.activity.PersonInfoActivity;
import com.gdkm.sfk.adapter.PersonLVAdapter;
import com.gdkm.sfk.application.LoginApplication;
import com.gdkm.sfk.utils.AsyncActionInvoker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/16.
 */
public class PersonFragment extends Fragment {
    private Context context;
    private View view;
    private Button btnLogin,btnRegister;
    private ImageButton imgBtnLogin;
    private TextView tvNickName;

    private ListView lvPersonFunction;
    private List<Map<String,Object>> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.person_fragment, container, false);
        context = getActivity();
        initData();
        initView();
        return view;

    }

    /**
     *初始化数据
     */
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
        map.put("text", "查看我的信息");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon",R.drawable.shenfenyanzheng);
        map.put("text", "实名验证");
        list.add(map);

    }

    /**
     * 初始化界面
     */
    private void initView() {
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new MClick());
        lvPersonFunction = (ListView) view.findViewById(R.id.lv_personFunction);
        PersonLVAdapter personLVAdapter = new PersonLVAdapter(context,list);
        lvPersonFunction.setAdapter(personLVAdapter);
        imgBtnLogin = (ImageButton) view.findViewById(R.id.imgBtn_login);
        imgBtnLogin.setOnClickListener(new MClick());
        tvNickName = (TextView) view.findViewById(R.id.tv_nickName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 监听事件
     */
    class MClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btn_login:
                    intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.imgBtn_login:
                    intent = new Intent(context, PersonInfoActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * 设置个人信息
     * 隐藏登录按钮
     * @param loginApplication
     */
    public void setPersonMessage(LoginApplication loginApplication){
        imgBtnLogin.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        tvNickName.setVisibility(View.VISIBLE);
        tvNickName.setText(loginApplication.getNickName());
        System.out.println("----setPersonMessage----" + loginApplication.getHeadPath());
        if (null==loginApplication.getHeadPath() && "".equals(loginApplication.getHeadPath())){
            imgBtnLogin.setBackgroundResource(R.drawable.morentouxiang);
        }else {
            GetHeadThread getHeadThread = new GetHeadThread(loginApplication.getHeadPath());
            getHeadThread.start();
        }

    }

    /**
     * 设置个人信息2
     * 显示登录按钮
     */
    public void setPersonMessage2() {
        imgBtnLogin.setVisibility(View.GONE);
        btnLogin.setVisibility(View.VISIBLE);
        tvNickName.setVisibility(View.GONE);
        tvNickName.setText(null);
    }

    /**
     * 获取头像线程
     */
    class GetHeadThread extends Thread{
        private String headPath;
        public GetHeadThread(String headPath) {
            this.headPath = headPath;
        }

        @Override
        public void run() {
            super.run();
            Bitmap headBitmap = AsyncActionInvoker.getbitmap(headPath);
            Message msg = new Message();
            msg.obj = headBitmap;
            msg.what = 4;
            handler.sendMessage(msg);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==4){
                Bitmap bitmap = (Bitmap) msg.obj;
                imgBtnLogin.setImageBitmap(bitmap);
            }else{
            }

        }
    };
}
