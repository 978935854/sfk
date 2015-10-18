package com.gdkm.sfk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdkm.sfk.R;
import com.gdkm.sfk.UI.RefreshableView;
import com.gdkm.sfk.adapter.SeekSfTopicAdapter;
import com.gdkm.sfk.application.LoginApplication;
import com.gdkm.sfk.listener.AddressOnClickListener;
import com.gdkm.sfk.listener.PickerOnClickListener;
import com.gdkm.sfk.pojo.Sfk;
import com.gdkm.sfk.service.SeekSFService;
import com.gdkm.sfk.view.ProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/5/27.
 */
public class SeekSFActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView seek_sf_topic_listView;
    private List<String> dataList,peopleNum_dataList;
    private List<Sfk> seekSFTopicList = new ArrayList<Sfk>();
    private SeekSFService seekSFService;
    private Button sex_btn,address_btn,peopleNum_btn, btnSF;
    private SeekSfTopicAdapter adapter;
    private RefreshableView refreshableView;
    private RelativeLayout select_to_refresh_head;
    private RelativeLayout loading_ProgressBar;
    private BroadcastReceiver pickerSendBroadcast;
    private ProgressDialog progressDialog;
    private TextView sfTitle;
    private int seekTid, tid;
    private int startPosition=0;    //开始条目
    private int endPosition=9;      //最后条目
    private boolean scrollStatus = false;   //判断是否滚动到底部
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek_sf_topic);
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTip("正在加载数据...");

        tid = getIntent().getIntExtra("tid",0);
        btnSF = (Button) findViewById(R.id.sf_btn);
        sfTitle = (TextView) findViewById(R.id.sf_title);
        if(tid ==1){
            seekTid=2;//数字调换，用来获取相反的数据，如沙子获取沙主发布的沙发单
            sfTitle.setText("我要沙发");
            btnSF.setText("申请沙发");
        }else{
            seekTid=1;//数字调换，用来获取相反的数据，如沙子获取沙主发布的沙发单
            sfTitle.setText("我有沙发");
            btnSF.setText("发布沙发");
        }
        btnSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginApplication application = (LoginApplication) getApplication();
                Intent intent = null;
                if (null == application.getNickName() || "".equals(application.getNickName())) {
                    intent = new Intent(SeekSFActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(SeekSFActivity.this, AddSfkActivity.class);
                    intent.putExtra("tid", tid);
                }
                startActivity(intent);
            }
        });
        //注册广播接收器
        pickerSendBroadcast = new PickerSendBroadcast();
        registerReceiver(pickerSendBroadcast, pickerfilter);
        loading_ProgressBar = (RelativeLayout) findViewById(R.id.loading_ProgressBar);
        loading_ProgressBar.setVisibility(View.GONE);
        seek_sf_topic_listView = (ListView) findViewById(R.id.seek_sf_topic_listView);
        select_to_refresh_head = (RelativeLayout) findViewById(R.id.select_to_refresh_head);
        loadFirstData();    //首次加载沙发单列表
        loadSelectData();   //选择加载沙发单列表

        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                startPosition = 0;
                endPosition = 9;
                loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
            }

            @Override
            public void onLoadMore(int mStartPosition, int mEndPosition) {
                startPosition = mStartPosition;
                endPosition = mEndPosition;
                System.out.println("mStartPosition:"+mStartPosition+",mEndPosition:"+mEndPosition+",scrollStatus:"+scrollStatus);
                scrollStatus = true;
                loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
            }
        }, 0);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(pickerSendBroadcast);
        super.onDestroy();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                progressDialog.show();
            }
        }
    };/*首次加载沙发单列表*/
    private void loadFirstData() {
        handler.sendEmptyMessage(1);
        //获取沙发单列表
        AsyncLoadFirstData asyncLoadFirstData = new AsyncLoadFirstData();
        asyncLoadFirstData.execute(seekTid);
    }

    /*首次异步加载*/
    class AsyncLoadFirstData extends AsyncTask<Integer,Void,List<Sfk>>{
        @Override
        protected List<Sfk> doInBackground(Integer... params) {
            try {
                seekSFService = new SeekSFService(SeekSFActivity.this);
                seekSFTopicList = seekSFService.getSeekSFTopicList(params[0],startPosition,endPosition);
//                Thread.sleep(1000);//模拟网络耗时时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return seekSFTopicList;
        }

        @Override
        protected void onPostExecute(List<Sfk> sfks) {
            super.onPostExecute(sfks);
            adapter = new SeekSfTopicAdapter(getApplication(),seekSFTopicList, R.layout.seek_sf_topic_list);
            seek_sf_topic_listView.setAdapter(adapter);

            //LisView渐变模糊度始终
            AlphaAnimation aa2 = new AlphaAnimation(0f,1.0f);
            //渐变时间
            aa2.setDuration(500);
            aa2.setRepeatCount(0);
            seek_sf_topic_listView.startAnimation(aa2);

            progressDialog.dismiss();
            seek_sf_topic_listView.setOnItemClickListener(SeekSFActivity.this);
        }
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
        //点击地点事件
        address_btn.setOnClickListener(new AddressOnClickListener(this,address_btn));
        //点击性别事件
        PickerOnClickListener pickerOnClickListener = new PickerOnClickListener(this,dataList,sex_btn,seekTid);
        sex_btn.setOnClickListener(pickerOnClickListener);

        peopleNum_dataList = new ArrayList<String>();
        peopleNum_dataList.add("全部(人数)");
        peopleNum_dataList.add("接待1人");
        peopleNum_dataList.add("接待2人");
        peopleNum_dataList.add("接待3人");
        peopleNum_dataList.add("接待3人以上");

        PickerOnClickListener peopleNum_pickerOnClickListener = new PickerOnClickListener(this,peopleNum_dataList,peopleNum_btn,seekTid);
        peopleNum_btn.setOnClickListener(peopleNum_pickerOnClickListener);
    }

    public void refreshListView(){
        handler.sendEmptyMessage(1);
        loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
    }

    //根据顶部搜索条件，加载listview菜单新数据
    private void loadNewData(){
        Sfk sfk = new Sfk();
        sfk.setSsex(sex_btn.getText()+"");

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

        if("地点(全部)".equals(address_btn.getText())){
            sfk.setSaddress("");
        }else {
            sfk.setSaddress(address_btn.getText().toString());
        }
        sfk.setTid(seekTid);
        sfk.setStartPosition(startPosition);
        sfk.setEndPosition(endPosition);
        AsyncLoadSeekList asyncLoadSeekList = new AsyncLoadSeekList();
        asyncLoadSeekList.execute(sfk);
    }
    /**
     *下拉lisview更新数据
     */
    class AsyncLoadSeekList extends AsyncTask<Sfk,Void, List<Sfk> >{
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected List<Sfk> doInBackground(Sfk... params) {
            List<Sfk> sfkList = null;     //发送数据到服务器端并返回沙发单
            try {
                sfkList = seekSFService.getSeekSFTopicListBySfk(params[0]);
//                Thread.sleep(1000);//模拟网络耗时时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return sfkList;
        }

        @Override
        protected void onPostExecute(List<Sfk> sfks) {
            if (sfks.size()==0){
                refreshableView.showLoadComplete();
            }
            if (scrollStatus) {
                scrollStatus=false;
            }else {
                seekSFTopicList.clear();                            //清除原有的listview数据源
            }
            seekSFTopicList.addAll(sfks);                        //listview数据源更新
            adapter.notifyDataSetChanged();                     //数据源更改，通知listview更新数据
            refreshableView.finishRefreshing();
            progressDialog.dismiss();
            super.onPostExecute(sfks);
        }
    }

    /*list监听*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int sid = seekSFTopicList.get(position).getSid();
        Intent intent=new Intent();
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        intent.setClass(this,SfInfoActivity.class);
        intent.putExtra("sfinfoID", seekSFTopicList.get(position).getSid());
        intent.putExtra("tid",seekTid);
        startActivity(intent);
    }

    /*选择搜索广播接收器*/
    IntentFilter pickerfilter = new IntentFilter("picker_seletedText");
    class PickerSendBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            tid = intent.getIntExtra("tid",0);
            if(tid ==1){
                seekTid=2;//数字调换，用来获取相反的数据，如沙子获取沙主发布的沙发单
                sfTitle.setText("我要沙发");
                btnSF.setText("申请沙发");
            }else{
                seekTid=1;//数字调换，用来获取相反的数据，如沙子获取沙主发布的沙发单
                sfTitle.setText("我有沙发");
                btnSF.setText("发布沙发");
            }
            refreshListView();
        }
    }


}
