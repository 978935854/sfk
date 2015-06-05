package com.sfk.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sfk.UI.RefreshableView;
import com.sfk.activity.R;
import com.sfk.activity.SfInfoActivity;
import com.sfk.adapter.Seek_sf_topic_adapter;
import com.sfk.listener.PickerOnClickListener;
import com.sfk.pojo.Sfk;
import com.sfk.service.SeekSFService;

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
    Handler refreshableHandler = new Handler();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.panic_fragment,container,false);
        //加载view组件
        load_view();
        seekSFTopicList = new ArrayList<Sfk>();
        adapter = new Seek_sf_topic_adapter(getActivity(),seekSFTopicList,R.layout.seek_sf_topic_list);
        seek_sf_topic_listView.setAdapter(adapter);
        //注册接收选择器的参数
        getActivity().registerReceiver(new PickerSendBroadcast(), filter);
        //选择加载沙发单列表
        loadSelectData();
        return view;
    }

    //加载view组件
    private void load_view() {
        seek_sf_topic_listView = (ListView) view.findViewById(R.id.seek_sf_topic_listView2);
        refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
        sex_btn = (Button) view.findViewById(R.id.sex_btn2);
        address_btn = (Button) view.findViewById(R.id.address_btn2);
        peopleNum_btn = (Button) view.findViewById(R.id.peopleNum_btn2);
    }

    //首次加载沙发单列表
    public void loadFirstData() {

        seekSFService = new SeekSFService(getActivity());
        //获取沙发单列表
        try {
            seekSFTopicList.clear();
            List<Sfk> list = seekSFService.getSeekSFTopicList();
            seekSFTopicList.addAll(list);
        } catch (InterruptedException e) {
            Log.i("InterruptedException2", "InterruptedException");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //list
        adapter.notifyDataSetChanged();
        seek_sf_topic_listView.setOnItemClickListener(this);
        Log.i("adapter_2",adapter.getCount()+","+seekSFTopicList.size());
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

        peopleNum_dataList = new ArrayList<String>();
        peopleNum_dataList.add("全部(人数)");
        peopleNum_dataList.add("接待1人");
        peopleNum_dataList.add("接待2人");
        peopleNum_dataList.add("接待3人");
        peopleNum_dataList.add("接待3人以上");

        PickerOnClickListener peopleNum_pickerOnClickListener = new PickerOnClickListener(getActivity(),peopleNum_dataList,peopleNum_btn);
        peopleNum_btn.setOnClickListener(peopleNum_pickerOnClickListener);
    }

    //广播接收选择器参数
    IntentFilter filter = new IntentFilter("picker_seletedText");
    class PickerSendBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int btn=0;
            btn = intent.getIntExtra("btn",0);

            if(sex_btn.getId()==btn || peopleNum_btn.getId()==btn){
                RelativeLayout select_refresh_relativeLayout = (RelativeLayout) view.findViewById(R.id.select_to_refresh_head);
                select_refresh_relativeLayout.setVisibility(View.VISIBLE);
                loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
                select_refresh_relativeLayout.setVisibility(View.GONE);
            }
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
            List<Sfk> sfkList = seekSFService.getSeekSFTopicListBySfk(sfk);     //发送数据到服务器端并返回沙发单
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
        intent.setClass(getActivity(),SfInfoActivity.class);
        startActivity(intent);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
