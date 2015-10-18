package com.gdkm.sfk.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gdkm.sfk.R;
import com.gdkm.sfk.UI.RefreshableView;
import com.gdkm.sfk.activity.SfInfoActivity;
import com.gdkm.sfk.adapter.SeekSfTopicAdapter;
import com.gdkm.sfk.listener.AddressOnClickListener;
import com.gdkm.sfk.listener.PickerOnClickListener;
import com.gdkm.sfk.pojo.Sfk;
import com.gdkm.sfk.service.SeekSFService;
import com.gdkm.sfk.view.ProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/5/16.
 */
public class PanicFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView LVSeekSfTopic;
    private List<String> dataList,peopleNum_dataList;
    private List<Sfk> seekSFTopicList;
    private SeekSFService seekSFService;
    private Button sex_btn,address_btn,peopleNum_btn;
    private View load_data_view;
    private SeekSfTopicAdapter adapter;
    private RefreshableView refreshableView;
    private RelativeLayout select_to_refresh_head;
    private RelativeLayout loading_ProgressBar;
    private ProgressDialog progressDialog;
    private View view;
    private int startPosition=0;    //开始条目
    private int endPosition=9;      //最后条目
    private boolean scrollStatus = false;   //判断是否滚动到底部
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.panic_fragment,container,false);
        //加载view组件
        load_view();
        //注册接收选择器的参数
        getActivity().registerReceiver(new PickerSendBroadcast(), pickerfilter);

        loadSelectData();
        return view;
    }

    //加载view组件
    private void load_view() {
        loading_ProgressBar = (RelativeLayout) view.findViewById(R.id.loading_ProgressBar);
        select_to_refresh_head = (RelativeLayout) view.findViewById(R.id.select_to_refresh_head);
        LVSeekSfTopic = (ListView) view.findViewById(R.id.seek_sf_topic_listView2);
        refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
        sex_btn = (Button) view.findViewById(R.id.sex_btn2);
        address_btn = (Button) view.findViewById(R.id.address_btn2);
        peopleNum_btn = (Button) view.findViewById(R.id.peopleNum_btn2);
        progressDialog = new com.gdkm.sfk.view.ProgressDialog(getActivity());
        progressDialog.setTip("正在加载数据...");

        seekSFTopicList = new ArrayList<Sfk>();
        adapter = new SeekSfTopicAdapter(getActivity(),seekSFTopicList,R.layout.seek_sf_topic_list);
        LVSeekSfTopic.setAdapter(adapter);

        //选择加载沙发单列表
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
                System.out.println("mStartPosition:" + mStartPosition + ",mEndPosition:" + mEndPosition + ",scrollStatus:" + scrollStatus);
                scrollStatus = true;
                loadNewData();   //根据顶部搜索条件，加载listview菜单新数据
            }
        }, 0);
    }

    /*首次加载沙发单列表*/
    public void loadFirstData() {
        progressDialog.show();
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
                seekSFTopicList = seekSFService.getSeekSFTopicList(params[0],startPosition,endPosition);
//                Thread.sleep(2000);//模拟网络耗时时间
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
            progressDialog.dismiss();

            //LisView渐变模糊度始终
            AlphaAnimation aa2 = new AlphaAnimation(0f,1.0f);
            //渐变时间
            aa2.setDuration(500);
            aa2.setRepeatCount(0);
            LVSeekSfTopic.startAnimation(aa2);

            adapter = new SeekSfTopicAdapter(getActivity(),seekSFTopicList,R.layout.seek_sf_topic_list);
            LVSeekSfTopic.setAdapter(adapter);
            LVSeekSfTopic.setOnItemClickListener(PanicFragment.this);
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

        PickerOnClickListener pickerOnClickListener = new PickerOnClickListener(getActivity(),dataList,sex_btn,2);
        sex_btn.setOnClickListener(pickerOnClickListener);
        //点击地点事件
        address_btn.setOnClickListener(new AddressOnClickListener(getActivity(),address_btn));

        peopleNum_dataList = new ArrayList<String>();
        peopleNum_dataList.add("全部(人数)");
        peopleNum_dataList.add("接待1人");
        peopleNum_dataList.add("接待2人");
        peopleNum_dataList.add("接待3人");
        peopleNum_dataList.add("接待3人以上");

        PickerOnClickListener peopleNum_pickerOnClickListener = new PickerOnClickListener(getActivity(),peopleNum_dataList,peopleNum_btn,2);
        peopleNum_btn.setOnClickListener(peopleNum_pickerOnClickListener);
    }

    public void refreshListView(){
        progressDialog.show();
//        select_to_refresh_head.setVisibility(View.VISIBLE);//添加listview加载数据进度
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
        sfk.setTid(2);
        sfk.setStartPosition(startPosition);
        sfk.setEndPosition(endPosition);
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
//                Thread.sleep(2000);//模拟网络耗时时间
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