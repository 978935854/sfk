package com.sfk.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sfk.activity.R;

import java.util.List;

/**
 * Created by zxw on 2015/6/8.
 */
public class ImageListAdapter extends BaseAdapter {

    private Activity activity;
    private List<String> imageList;

    public ImageListAdapter(Activity activity, List<String> imageList) {
        this.activity = activity;
        this.imageList = imageList;
    }

    public int getCount() {
        return imageList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        String url;
        if(convertView==null){
            convertView = View.inflate(activity, R.layout.bbs_image,null);
            imageView=(ImageView)convertView.findViewById(R.id.bbs_image_one);
            TopicListAdapter.ViewCache viewCache=new TopicListAdapter.ViewCache();
            viewCache.imageView=imageView;
            convertView.setTag(viewCache);
        }else{
            TopicListAdapter.ViewCache viewCache=(TopicListAdapter.ViewCache)convertView.getTag();
           imageView=viewCache.imageView;
        }
        url=imageList.get(position);
        Bitmap bm = BitmapFactory.decodeFile(url);
        imageView.setImageBitmap(bm);
        return convertView;
    }

}
