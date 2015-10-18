package com.gdkm.sfk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gdkm.sfk.R;
import com.gdkm.sfk.pojo.Photo;
import com.gdkm.sfk.utils.ImageLoader;
import com.gdkm.sfk.utils.ScollListview;

import java.util.List;

/**
 * Created by Administrator on 2015/6/10.
 */
public class SfinfoAdapter extends BaseAdapter{
    private List<Photo> mList;
    private LayoutInflater mInflater;
    private ImageLoader mimageLoader;
    private int mStart,mEnd;
    public static String[] URLS;
    private boolean mFirst;

    public SfinfoAdapter(Context context,List<Photo> data,ScollListview listView){
        mList=data;
        mInflater=LayoutInflater.from(context);
        mimageLoader= new ImageLoader(listView);
        URLS = new String[data.size()];
        for(int i=0;i<data.size();i++){
            URLS[i]=data.get(i).getPath();
        }
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView=mInflater.inflate(R.layout.image_view_item,null);
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.sfinfophoto);
            convertView.setTag(viewHolder);

//            if(URLS.length==1){//判断如果只有一张图片时设置一张背景图片让list有高度
//                viewHolder.imageView.setImageResource(R.drawable.pictures_no);
//            }
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.drawable.pictures_no);
        String url=mList.get(position).getPath();
        viewHolder.imageView.setTag(url);

        mimageLoader.showImageviewAsyncTask(viewHolder.imageView, mList.get(position).getPath());

        return convertView;
    }

    private class ViewHolder{
        public ImageView imageView;
    }

}
