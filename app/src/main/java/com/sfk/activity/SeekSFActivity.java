package com.sfk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.sfk.UI.PickerView;
import com.sfk.adapter.Seek_sf_topic_adapter;
import com.sfk.listener.PickerOnClickListener;
import com.sfk.pojo.Sfk;
import com.sfk.service.SeekSFService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek_sf_topic);
        //注册接收选择器的参数
        registerReceiver(new PickerSendBroadcast(), filter);

        seekSFService = new SeekSFService();
        //获取沙发单列表
        try {
            seekSFTopicList = seekSFService.getSeekSFTopicList();
        } catch (InterruptedException e) {
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

        dataList = new ArrayList<String>();
        dataList.add("全部(性别)");
        dataList.add("男");
        dataList.add("女");
        dataList.add("男女不限");

        sex_btn = (Button) findViewById(R.id.sex_btn);
        address_btn = (Button) findViewById(R.id.address_btn);
        peopleNum_btn = (Button) findViewById(R.id.peopleNum_btn);

        PickerOnClickListener pickerOnClickListener = new PickerOnClickListener(this,dataList,R.id.sex_btn);
        sex_btn.setOnClickListener(pickerOnClickListener);

        peopleNum_dataList = new ArrayList<String>();
        peopleNum_dataList.add("全部(人数)");
        peopleNum_dataList.add("接待1人");
        peopleNum_dataList.add("接待2人");
        peopleNum_dataList.add("接待3人");
        peopleNum_dataList.add("接待3人以上");

        PickerOnClickListener peopleNum_pickerOnClickListener = new PickerOnClickListener(this,peopleNum_dataList,R.id.peopleNum_btn);
        peopleNum_btn.setOnClickListener(peopleNum_pickerOnClickListener);

    }

    //广播接收选择器参数
    IntentFilter filter = new IntentFilter("picker_seletedText");


    class PickerSendBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String selectedText = intent.getStringExtra("selectedText");
            int btn = intent.getIntExtra("btn",0);
            Sfk sfk = new Sfk();
            if(btn==R.id.sex_btn){
                sex_btn.setText(selectedText);
                    int ssex = 0;
                    if("女".equals(selectedText)){
                        ssex=1;
                    }else if("男".equals(selectedText)){
                        ssex=2;
                    }else if("男女不限".equals(selectedText)){
                        ssex=3;
                    }
                    sfk.setSsex(ssex);
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
            }
            else if(btn==R.id.peopleNum_btn){
                peopleNum_btn.setText(selectedText);
                if("全部(人数)".equals(selectedText)){
                    sfk.setSpeoplenum(0);
                }else if("接待1人".equals(selectedText)){
                    sfk.setSpeoplenum(1);
                }else if("接待2人".equals(selectedText)){
                    sfk.setSpeoplenum(2);
                }else if("接待3人".equals(selectedText)){
                    sfk.setSpeoplenum(3);
                }else if("接待3人以上".equals(selectedText)){
                    sfk.setSpeoplenum(4);
                }

                int ssex = 0;
                if("女".equals(sex_btn.getText())){
                    ssex=1;
                }else if("男".equals(sex_btn.getText())){
                    ssex=2;
                }else if("男女不限".equals(sex_btn.getText())){
                    ssex=3;
                }
                sfk.setSsex(ssex);
            }
            if("地点".equals(address_btn.getText())){
                sfk.setSaddress("");
            }

            try {
                seek_sf_topic_listView.addHeaderView(load_data_view);//添加listview加载数据进度条
                seekSFTopicList.clear();                            //清除原有的listview数据源
                List<Sfk> sfkList = seekSFService.getSeekSFTopicListBySfk(sfk);     //发送数据到服务器端并返回沙发单
                seekSFTopicList.addAll(sfkList);                    //listview数据源更新
                adapter.notifyDataSetChanged();                     //数据源更改，通知listview更新数据
                seek_sf_topic_listView.removeHeaderView(load_data_view);//加载完listview数据，关闭数据进度条
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //list监听
        Intent intent=new Intent();
        intent.setClass(this,SfInfoActivity.class);
        startActivity(intent);

    }

}
