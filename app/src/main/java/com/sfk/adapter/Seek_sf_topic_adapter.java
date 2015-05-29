package com.sfk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sfk.activity.R;
import com.sfk.pojo.Sfk;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class Seek_sf_topic_adapter extends BaseAdapter {
    private Context context;
    private List<Sfk> seekSFTopicList;
    private int resoure;
    private LayoutInflater inflater;

    public Seek_sf_topic_adapter(Context context, List<Sfk> seekSFTopicList, int resoure) {
        this.context = context;
        this.seekSFTopicList = seekSFTopicList;
        this.resoure = resoure;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return seekSFTopicList.size();
    }

    @Override
    public Object getItem(int position) {
        return seekSFTopicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title,time,address,sf_sex,sftype;
        if (convertView==null){
            convertView = inflater.inflate(resoure,null);
            title = (TextView) convertView.findViewById(R.id.title);
            time = (TextView)convertView.findViewById(R.id.time);
            address = (TextView)convertView.findViewById(R.id.address);
            sf_sex = (TextView)convertView.findViewById(R.id.sf_sex);
            sftype = (TextView)convertView.findViewById(R.id.sftype);
            convertView.setTag(new DataWrapper(title,time,address,sf_sex,sftype));
        }else{
            DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
            title = dataWrapper.title;
            time = dataWrapper.time;
            address = dataWrapper.address;
            sf_sex = dataWrapper.sf_sex;
            sftype = dataWrapper.sftype;
        }
        title.setText(seekSFTopicList.get(position).getStitle());
        time.setText(seekSFTopicList.get(position).getStime());
        address.setText(seekSFTopicList.get(position).getSaddress());
        switch (seekSFTopicList.get(position).getSsex()){
            case 0:
                sf_sex.setText("只接待女");
                break;
            case 1:
                sf_sex.setText("只接待男");
                break;
            case 2:
                sf_sex.setText("男女不限");
                break;
            default:
                break;
        }

        sftype.setText(seekSFTopicList.get(position).getStype()+"");
        return convertView;
    }

    //存放组件常量
    private final class DataWrapper{
        TextView title,time,address,sf_sex,sftype;
        private DataWrapper(TextView title, TextView time, TextView address, TextView sf_sex, TextView sftype) {
            this.title = title;
            this.time = time;
            this.address = address;
            this.sf_sex = sf_sex;
            this.sftype = sftype;
        }
    }

}
