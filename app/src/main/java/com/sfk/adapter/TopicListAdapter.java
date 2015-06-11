package com.sfk.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sfk.UI.BBS_list_ImageText;
import com.sfk.activity.R;
import com.sfk.pojo.Topic;
import com.sfk.service.BBSservice;
import com.sfk.utils.BaseProtocolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxw on 2015/6/4.
 */
public class TopicListAdapter extends BaseAdapter {
    private Activity activity;
    //    private int resource;
    private List<Topic> topicList;
    private List<List<String>>  PhotoList;

    //    private LayoutInflater inflater;
    public TopicListAdapter(Activity activity, List<Topic> topicList, List<List<String>>  PhotoList) {
        this.activity = activity;
        this.topicList = topicList;
        this.PhotoList=PhotoList;

/*        this.resource = resource;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
    }
    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*BBS_list_ImageText bbs_topicList=null;*/
        List<String> imageList;
        TextView topicTime,topicUserName,topicTitle,topicContent;
        ListView listView;
        if(convertView==null){
            convertView = View.inflate(activity,R.layout.bbs_list_text,null);
            topicTime=(TextView)convertView.findViewById(R.id.bbs_topicTime);
            topicTitle=(TextView)convertView.findViewById(R.id.bbs_topicTitle);
            topicUserName=(TextView)convertView.findViewById(R.id.bbs_userName);
            topicContent=(TextView)convertView.findViewById(R.id.bbs_topic_content);
            ViewCache viewCache=new ViewCache();
            viewCache.topicTime=topicTime;
            viewCache.topicTitle=topicTitle;
            viewCache.topicUserName=topicUserName;
            viewCache.topicContent=topicContent;
            convertView.setTag(viewCache);
            /*convertView.setTag(new DataWrapper(bbs_topicList));*/
        }else{
            ViewCache viewCache=(ViewCache)convertView.getTag();
            topicTime=viewCache.topicTime;
            topicTitle=viewCache.topicTitle;
            topicUserName=viewCache.topicUserName;
            topicContent=viewCache.topicContent;
        }
        Topic topic= topicList.get(position);
        //下面代码实现数据绑定
        topicTime.setText(topic.getTreleasetime());
        topicTitle.setText(topic.getTopictitle());
        topicUserName.setText(topic.getUsername());
        topicContent.setText(topic.getTopiccontent());

        imageList=new ArrayList<String>();
        imageList=PhotoList.get(position);

        if(!imageList.get(0).equals("NO")) {
            listView = (ListView) activity.findViewById(R.id.bbs_imageListView);
            ImageListAdapter imageListAdapter = new ImageListAdapter(activity, imageList);
            listView.setAdapter(imageListAdapter);
        }


        Log.i("time1",topic.getTreleasetime());
        return convertView;
    }

    public static class ViewCache{
        TextView topicTime,topicUserName,topicTitle,topicContent;
        ImageView imageView;
    }

/*    public static void AsyncImage(ViewCache viewCache,String iamgeUrl){
        ImageAsyncTask imageAsyncTask=new ImageAsyncTask(viewCache);
    }*/

}
