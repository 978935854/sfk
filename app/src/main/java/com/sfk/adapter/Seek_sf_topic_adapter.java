package com.sfk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sfk.activity.R;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/5/27.
 */
public class Seek_sf_topic_adapter extends BaseAdapter {
    private Context context;
    private HashMap<String,Object> sf_topicMap;
    private int resoure;
    private LayoutInflater inflater;

    public Seek_sf_topic_adapter(Context context, HashMap<String, Object> sf_topicMap, int resoure) {
        this.context = context;
        this.sf_topicMap = sf_topicMap;
        this.resoure = resoure;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return sf_topicMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = inflater.inflate(resoure,null);
        }
        return convertView;
    }
}
