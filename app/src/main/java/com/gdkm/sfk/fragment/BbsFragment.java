package com.gdkm.sfk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gdkm.sfk.R;
import com.gdkm.sfk.adapter.TopicListAdapter;
import com.gdkm.sfk.pojo.Topic;
import com.gdkm.sfk.utils.AsyncActionInvoker;
import com.gdkm.sfk.view.ProgressDialog;
import com.gdkm.sfk.view.ScollListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/5/16.
 */
public class BbsFragment extends Fragment {
    private View view;
    private Context context;
    public static boolean alreadUpdate=true;
    private ScollListView topicListView;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bbs_fragment, container, false);
        context = getActivity();
        initView();
//        initData();
        return view;
    }

    public void initData() {
        handler.sendEmptyMessage(0x1);
        getTopicList();
    }

    private List<Topic> getTopicList(){
        final List<Topic> topicsList = new ArrayList<Topic>();
        AsyncActionInvoker ai = new AsyncActionInvoker();
        ai.setRequestMethod("GET");
        ai.submitMessage("topic/TopicAction!findTopicList");
        ai.setOnTextClickListener(new AsyncActionInvoker.OnTextClickListener() {
            @Override
            public void returnMessage(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    JSONArray topicArray = (JSONArray) jsonObject.get("TopicList");
                    Gson gson = new Gson();
                    for (int i = 0; i < topicArray.length(); i++) {
                        Topic topic = gson.fromJson(topicArray.get(i).toString(), Topic.class);
                        topicsList.add(topic);
                        System.out.println("----text----" + topic.getPhotoList());
                    }
                    Message msg = new Message();
                    msg.what=0x2;
                    msg.obj = topicsList;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return topicsList;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0x1){
                progressDialog.show();
            }else if (msg.what==0x2){
                progressDialog.dismiss();
                List<Topic> topicsList = (List<Topic>) msg.obj;
                TopicListAdapter topicListAdapter=new TopicListAdapter(context,topicsList);
                topicListView.setAdapter(topicListAdapter);
            }
        }
    };

    private void initView() {
        topicListView = (ScollListView) view.findViewById(R.id.bbs_TopicList);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTip("正在加载数据...");
    }

}
