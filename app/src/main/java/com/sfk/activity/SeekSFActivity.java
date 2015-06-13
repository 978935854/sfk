package com.sfk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sfk.UI.PickerView;
import com.sfk.UI.RefreshableView;
import com.sfk.adapter.Seek_sf_topic_adapter;
import com.sfk.listener.AddressOnClickListener;
import com.sfk.listener.PickerOnClickListener;
import com.sfk.pojo.Sfk;
import com.sfk.service.SeekSFService;
import com.sfk.service.SfInfoService;

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
    Button sex_btn,address_btn,peopleNum_btn,sf_btn;
    Seek_sf_topic_adapter adapter;
    RefreshableView refreshableView;
    RelativeLayout select_to_refresh_head;
    int tid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek_sf_topic);
        tid = getIntent().getIntExtra("tid",0);
        sf_btn = (Button) findViewById(R.id.sf_btn);
        TextView sf_title = (TextView) findViewById(R.id.sf_title);
        if(tid==1){
            tid=2;//数字调换，用来获取相反的数据，如沙子获取沙主发布的沙发单
            sf_title.setText("我要沙发");
            sf_btn.setText("申请沙发");
        }else{
            tid=1;//数字调换，用来获取相反的数据，如沙子获取沙主发布的沙发单
            sf_title.setText("我有沙发");
            sf_btn.setText("发布沙发");
        }
        sf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeekSFActivity.this,AddSfkActivity.class);
                intent.putExtra("tid",tid);
                startActivity(intent);
            }
        });
        //注册广播接收器
        registerReceiver(new PickerSendBroadcast(),pickerfilter);
        seek_sf_topic_listView = (ListView) findViewById(R.id.seek_sf_topic_listView);
        select_to_refresh_head = (RelativeLayout) findViewById(R.id.select_to_refresh_head);
        loadFirstData();    //首次加载沙发单列表
        loadSelectData();   //选择加载沙发单列表

        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
            }
        }, 0);
    }

    /*首次加载沙发单列表*/
    private void loadFirstData() {
        select_to_refresh_head.setVisibility(View.VISIBLE);//添加listview加载数据进度条
        //获取沙发单列表
        AsyncLoadFirstData asyncLoadFirstData = new AsyncLoadFirstData();
        asyncLoadFirstData.execute(tid);
    }

    /*首次异步加载*/
    class AsyncLoadFirstData extends AsyncTask<Integer,Void,List<Sfk>>{
        @Override
        protected List<Sfk> doInBackground(Integer... params) {
            try {
                seekSFService = new SeekSFService(SeekSFActivity.this);
                seekSFTopicList = seekSFService.getSeekSFTopicList(params[0]);
                Thread.sleep(2000);//模拟网络耗时时间
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
            adapter = new Seek_sf_topic_adapter(getApplication(),seekSFTopicList,R.layout.seek_sf_topic_list);
            seek_sf_topic_listView.setAdapter(adapter);
            select_to_refresh_head.setVisibility(View.GONE);//加载完listview关闭数据进度条
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

    public void refreshListView(){
        select_to_refresh_head.setVisibility(View.VISIBLE);//添加listview加载数据进度
        loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
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
        sfk.setTid(tid);
        AsyncLoadSeekList asyncLoadSeekList = new AsyncLoadSeekList();
        asyncLoadSeekList.execute(sfk);
    }
    //下拉lisview更新数据
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
                Thread.sleep(2000);//模拟网络耗时时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return sfkList;
        }

        @Override
        protected void onPostExecute(List<Sfk> sfks) {
            seekSFTopicList.clear();                            //清除原有的listview数据源
            seekSFTopicList.addAll(sfks);                        //listview数据源更新
            adapter.notifyDataSetChanged();                     //数据源更改，通知listview更新数据
            select_to_refresh_head.setVisibility(View.GONE);    //移除listview加载数据进度条
            refreshableView.finishRefreshing();
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
        startActivity(intent);
    }

    /*选择搜索广播接收器*/
    IntentFilter pickerfilter = new IntentFilter("picker_seletedText");
    class PickerSendBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListView();
        }
    }

}
