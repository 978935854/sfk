package com.gdkm.sfk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdkm.sfk.R;
import com.gdkm.sfk.pojo.Photo;
import com.gdkm.sfk.utils.ImageLoader;
import com.gdkm.sfk.utils.ScollListview;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/10.
 */
public class PersonLVAdapter extends BaseAdapter{
    private List<Map<String,Object>> list;
    private LayoutInflater mInflater;

    public PersonLVAdapter(Context context, List<Map<String,Object>> list){
        mInflater=LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder =new ViewHolder();
            convertView=mInflater.inflate(R.layout.person_lv_item,null);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.iv);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(list.get(position).get("text").toString());
        viewHolder.imageView.setBackgroundResource(Integer.parseInt(list.get(position).get("icon").toString()));
        return convertView;
    }

    private class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

}
