package com.sfk.service;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.sfk.Constant.Constant;
import com.sfk.activity.MainActivity;
import com.sfk.activity.R;
import com.sfk.adapter.TopicListAdapter;
import com.sfk.pojo.Topic;
import com.sfk.utils.BaseProtocolUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxw on 2015/5/31.
 */
public class BBSservice {
    public static boolean alreadUpdate=true;
    public static  List<Topic> topicList=null;
    ListView topicListView;
    MainActivity mainActivity;

    public BBSservice(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void bbsService() throws Exception {
        alreadUpdate=false;
        GetTopicListThread getTopicListThread = new GetTopicListThread();
        getTopicListThread.start();
        getTopicListThread.join();
        topicList = getTopicListThread.topicList;

        TopicListAdapter topicListAdapter=new TopicListAdapter(mainActivity,topicList, R.layout.bbs_list_text);
        topicListView=(ListView)mainActivity.findViewById(R.id.bbs_TopicList);
        topicListView.setAdapter(topicListAdapter);

    }



    //获取帖子列表线程
    class GetTopicListThread extends Thread {
        List<Topic> topicList;

        @Override
        public void run() {
            super.run();
            final BaseProtocolUtil baseProtocolUtil1 = new BaseProtocolUtil();
            String url = Constant.projectServicePath + "topic/TopicAction!findTopicList";
            try {
                baseProtocolUtil1.packGet(url);
                baseProtocolUtil1.parse();
                JSONArray topicJSONArray=baseProtocolUtil1.getJSONArray("TopicList");


                Log.i("topicJSONArray", topicJSONArray.toString());
                topicList = new ArrayList<Topic>();
                for (int i = 0; i < topicJSONArray.length(); i++) {
                    Topic topic = new Topic();
                    JSONObject object = (JSONObject) topicJSONArray.get(i);
                    topic.setTopicid(object.getInt("cid"));
                    topic.setTopictitle(object.getString("topictitle"));
                    topic.setUsername(object.getString("ccusername"));
                    topic.setTreleasetime(object.getString("treleasetime"));
                    topicList.add(topic);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}