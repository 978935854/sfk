package com.sfk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.sfk.adapter.Seek_sf_topic_adapter;
import com.sfk.pojo.Sfk;
import com.sfk.service.SeekSFService;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class SeekSFActivity extends Activity {
    private ListView seek_sf_topic_listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek_sf_topic);

        SeekSFService seekSFService = new SeekSFService();
        //获取沙发单列表
        try {
            List<Sfk> seekSFTopicList = seekSFService.getSeekSFTopicList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String,Object> map = new HashMap<String, Object>();
        seek_sf_topic_listView = (ListView) findViewById(R.id.seek_sf_topic_listView);
        Seek_sf_topic_adapter adapter = new Seek_sf_topic_adapter(getApplication(),map,R.layout.seek_sf_topic_list);
        seek_sf_topic_listView.setAdapter(adapter);
    }
}
