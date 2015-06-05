package com.sfk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sfk.UI.PickerView;
import com.sfk.UI.RefreshableView;
import com.sfk.adapter.Seek_sf_topic_adapter;
import com.sfk.listener.PickerOnClickListener;
import com.sfk.pojo.Sfk;
import com.sfk.service.SeekSFService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/5/27.
 */
public class SeekSFActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView seek_sf_topic_listView;
    List<String> dataList,peopleNum_dataList;
    private List<Sfk> seekSFTopicList = new ArrayList<Sfk>();
    SeekSFService seekSFService;
    Button sex_btn,address_btn,peopleNum_btn;
    View load_data_view;
    Seek_sf_topic_adapter adapter;
    RefreshableView refreshableView;
    Handler refreshableHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek_sf_topic);
        Log.i("currenttime",System.currentTimeMillis()+"");
        //注册接收选择器的参数
        registerReceiver(new PickerSendBroadcast(), filter);

        loadFirstData();    //首次加载沙发单列表
        loadSelectData();   //选择加载沙发单列表
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                refreshableHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
                    }
                });
                refreshableView.finishRefreshing();
            }
        }, 0);

    }

    //首次加载沙发单列表
    private void loadFirstData() {
        seekSFService = new SeekSFService(this);
        //获取沙发单列表
        try {long time_1 = System.currentTimeMillis();
            seekSFTopicList = seekSFService.getSeekSFTopicList();
            long time_2 = System.currentTimeMillis();
            Log.i("currenttime",(time_2-time_1)+"");
        } catch (InterruptedException e) {
            Log.i("InterruptedException2","InterruptedException");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //list
        seek_sf_topic_listView = (ListView) findViewById(R.id.seek_sf_topic_listView);
        adapter = new Seek_sf_topic_adapter(getApplication(),seekSFTopicList,R.layout.seek_sf_topic_list);
        load_data_view = this.getLayoutInflater().inflate(R.layout.load_data_style,null);
        seek_sf_topic_listView.addHeaderView(load_data_view);   //添加listview加载数据进度条
        seek_sf_topic_listView.setAdapter(adapter);
        seek_sf_topic_listView.removeHeaderView(load_data_view);////加载完listview关闭数据进度条
        seek_sf_topic_listView.setOnItemClickListener(this);
    }

    //选择加载沙发单列表
    private void loadSelectData() {
        dataList = new ArrayList<String>();
        dataList.add("接待(全部)");
        dataList.add("接待(男)");
        dataList.add("接待(女)");
        dataList.add("男女不限");

        sex_btn = (Button) findViewById(R.id.sex_btn);
        address_btn = (Button) findViewById(R.id.address_btn);
        peopleNum_btn = (Button) findViewById(R.id.peopleNum_btn);

        PickerOnClickListener pickerOnClickListener = new PickerOnClickListener(this,dataList,sex_btn);
        sex_btn.setOnClickListener(pickerOnClickListener);

        peopleNum_dataList = new ArrayList<String>();
        peopleNum_dataList.add("全部(人数)");
        peopleNum_dataList.add("接待1人");
        peopleNum_dataList.add("接待2人");
        peopleNum_dataList.add("接待3人");
        peopleNum_dataList.add("接待3人以上");

        PickerOnClickListener peopleNum_pickerOnClickListener = new PickerOnClickListener(this,peopleNum_dataList,peopleNum_btn);
        peopleNum_btn.setOnClickListener(peopleNum_pickerOnClickListener);
    }

    //广播接收选择器参数
    IntentFilter filter = new IntentFilter("picker_seletedText");


    class PickerSendBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RelativeLayout select_refresh_relativeLayout = (RelativeLayout) findViewById(R.id.select_to_refresh_head);
            select_refresh_relativeLayout.setVisibility(View.VISIBLE);
            Log.i("select_refresht",select_refresh_relativeLayout.getVisibility()+"");
            loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
            select_refresh_relativeLayout.setVisibility(View.GONE);
            Log.i("select_refresht",select_refresh_relativeLayout.getVisibility()+"");
        }
    }
    //根据顶部搜索条件，加载listview菜单新数据
    private void loadNewData(){
        Sfk sfk = new Sfk();
        if("接待(女)".equals(sex_btn.getText())){
            sfk.setSsex(1);
        }else if("接待(男)".equals(sex_btn.getText())){
            sfk.setSsex(2);
        }else if("男女不限".equals(sex_btn.getText())){
            sfk.setSsex(3);
        }else{
            sfk.setSsex(0);
        }

        Log.i("ssex22",sfk.getSsex().toString());

        if("全部(人数)".equals(peopleNum_btn.getText())){
            sfk.setSpeoplenum(0);
        }else if("接待1人".equals(peopleNum_btn.getText())){
            sfk.setSpeoplenum(1);
        }else if("接待2人".equals(peopleNum_btn.getText())){
            sfk.setSpeoplenum(2);
        }else if("接待3人".equals(peopleNum_btn.getText())){
            sfk.setSpeoplenum(3);
        }else if("接待3人以上".equals(peopleNum_btn.getText())){
            sfk.setSpeoplenum(4);
        }

        if("地点".equals(address_btn.getText())){
            sfk.setSaddress("");
        }else {
            sfk.setSaddress(address_btn.getText().toString());
        }

        try {
            seekSFTopicList.clear();                            //清除原有的listview数据源
            long time_1 = System.currentTimeMillis();
            List<Sfk> sfkList = seekSFService.getSeekSFTopicListBySfk(sfk);     //发送数据到服务器端并返回沙发单
            long time_2 = System.currentTimeMillis();
            Log.i("currenttime_2",time_2-time_1+"");
            seekSFTopicList.addAll(sfkList);                    //listview数据源更新
            adapter.notifyDataSetChanged();                     //数据源更改，通知listview更新数据
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //list监听
        Intent intent=new Intent();
        intent.setClass(this,SfInfoActivity.class);
        startActivity(intent);
    }

    //监听返回键，若返回直接关闭该activity
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
            finish();
        }
        return true;
    }

}
