package com.sfk.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sfk.UI.PickerView;
import com.sfk.UI.RefreshableView;
import com.sfk.activity.R;
import com.sfk.activity.SfInfoActivity;
import com.sfk.adapter.Seek_sf_topic_adapter;
import com.sfk.listener.AddressOnClickListener;
import com.sfk.listener.PickerOnClickListener;
import com.sfk.pojo.Sfk;
import com.sfk.service.SeekSFService;
import com.sfk.service.SfInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/5/16.
 */
public class Panic_Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView seek_sf_topic_listView;
    List<String> dataList,peopleNum_dataList;
    private List<Sfk> seekSFTopicList;
    SeekSFService seekSFService;
    Button sex_btn,address_btn,peopleNum_btn;
    View load_data_view;
    Seek_sf_topic_adapter adapter;
    RefreshableView refreshableView;
    RelativeLayout select_to_refresh_head;
    LinearLayout loading_ProgressBar;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.panic_fragment,container,false);
        //加载view组件
        load_view();
        //注册接收选择器的参数
        getActivity().registerReceiver(new PickerSendBroadcast(),pickerfilter);
        seekSFTopicList = new ArrayList<Sfk>();
        adapter = new Seek_sf_topic_adapter(getActivity(),seekSFTopicList,R.layout.seek_sf_topic_list);
        seek_sf_topic_listView.setAdapter(adapter);

        //选择加载沙发单列表
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
            }
        }, 0);
        loadSelectData();
        return view;
    }

    //加载view组件
    private void load_view() {
        loading_ProgressBar = (LinearLayout) view.findViewById(R.id.loading_ProgressBar);
        select_to_refresh_head = (RelativeLayout) view.findViewById(R.id.select_to_refresh_head);
        seek_sf_topic_listView = (ListView) view.findViewById(R.id.seek_sf_topic_listView2);
        refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
        sex_btn = (Button) view.findViewById(R.id.sex_btn2);
        address_btn = (Button) view.findViewById(R.id.address_btn2);
        peopleNum_btn = (Button) view.findViewById(R.id.peopleNum_btn2);
    }

    /*首次加载沙发单列表*/
    public void loadFirstData() {
        loading_ProgressBar.setVisibility(View.VISIBLE);//添加圆形滚动条
//        select_to_refresh_head.setVisibility(View.VISIBLE);//添加listview加载数据进度条
        //获取沙发单列表
        AsyncLoadFirstData asyncLoadFirstData = new AsyncLoadFirstData();
        asyncLoadFirstData.execute(2);
    }

    /*首次异步加载*/
    class AsyncLoadFirstData extends AsyncTask<Integer,Void,List<Sfk>> {
        @Override
        protected List<Sfk> doInBackground(Integer... params) {
            try {
                seekSFService = new SeekSFService(getActivity());
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

            //图片渐变模糊度始终
            AlphaAnimation aa = new AlphaAnimation(1.0f,0f);
            //渐变时间
            aa.setDuration(500);
            aa.setRepeatCount(0);
            loading_ProgressBar.startAnimation(aa);

            //LisView渐变模糊度始终
            AlphaAnimation aa2 = new AlphaAnimation(0f,1.0f);
            //渐变时间
            aa2.setDuration(500);
            aa2.setRepeatCount(0);
            seek_sf_topic_listView.startAnimation(aa2);

            adapter = new Seek_sf_topic_adapter(getActivity(),seekSFTopicList,R.layout.seek_sf_topic_list);
            seek_sf_topic_listView.setAdapter(adapter);
            loading_ProgressBar.setVisibility(View.GONE);//加载完listview关闭数据圆形滚动条
//            select_to_refresh_head.setVisibility(View.GONE);//加载完listview关闭数据进度条
            seek_sf_topic_listView.setOnItemClickListener(Panic_Fragment.this);
        }
    }

    //选择加载沙发单列表
    private void loadSelectData() {
        seekSFService = new SeekSFService(getActivity());
        dataList = new ArrayList<String>();
        dataList.add("接待(全部)");
        dataList.add("接待(男)");
        dataList.add("接待(女)");
        dataList.add("男女不限");

        PickerOnClickListener pickerOnClickListener = new PickerOnClickListener(getActivity(),dataList,sex_btn);
        sex_btn.setOnClickListener(pickerOnClickListener);
        //点击地点事件
        address_btn.setOnClickListener(new AddressOnClickListener(getActivity(),address_btn));

        peopleNum_dataList = new ArrayList<String>();
        peopleNum_dataList.add("全部(人数)");
        peopleNum_dataList.add("接待1人");
        peopleNum_dataList.add("接待2人");
        peopleNum_dataList.add("接待3人");
        peopleNum_dataList.add("接待3人以上");

        PickerOnClickListener peopleNum_pickerOnClickListener = new PickerOnClickListener(getActivity(),peopleNum_dataList,peopleNum_btn);
        peopleNum_btn.setOnClickListener(peopleNum_pickerOnClickListener);
    }

    public void refreshListView(){
        select_to_refresh_head.setVisibility(View.VISIBLE);//添加listview加载数据进度
        loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
    }

    //广播接收选择器参数
    IntentFilter pickerfilter = new IntentFilter("picker_seletedText");
    class PickerSendBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int btn=0;
            btn = intent.getIntExtra("btn",0);
            Log.i("btn_id",btn+"");
            refreshListView();    //根据顶部搜索条件，加载listview菜单新数据
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
        sfk.setTid(2);
        AsyncLoadSeekList asyncLoadSeekList = new AsyncLoadSeekList();
        asyncLoadSeekList.execute(sfk);
    }
    //下拉lisview更新数据
    class AsyncLoadSeekList extends AsyncTask<Sfk,Void, List<Sfk> >{
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int sid = seekSFTopicList.get(position).getSid();
        Intent intent=new Intent();
        intent.setClass(getActivity(),SfInfoActivity.class);
        intent.putExtra("sfinfoID", sid);
        intent.putExtra("tid",3);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}