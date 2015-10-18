package com.gdkm.sfk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gdkm.sfk.R;
import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.utils.ImageZipUtil;
import com.gdkm.sfk.utils.PostMultipart;
import com.gdkm.sfk.view.ScollGridlView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/2.
 */
public class AddPhotoGVAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;
    private LayoutInflater mInflater;
    private int resource;

    //    private ViewHolder viewHolder = null;
    public AddPhotoGVAdapter(Context context, List<Map<String, Object>> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
        mInflater = LayoutInflater.from(context);
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
        ImageView imageView;
        if(parent instanceof ScollGridlView){
            if(((ScollGridlView) parent).isMeasure()){
                convertView = mInflater.inflate(resource, null);
                imageView = (ImageView) convertView.findViewById(R.id.iv_add);
                convertView.setTag(new ViewHolder(imageView));
                return convertView;
            }else{
                convertView = loadView(convertView,position);
            }
        }
        return convertView;
    }

    private View loadView(View convertView, int position) {
        ImageView imageView = null;
        if (convertView == null) {
            convertView = mInflater.inflate(resource, null);
            imageView = (ImageView) convertView.findViewById(R.id.iv_add);
            convertView.setTag(new ViewHolder(imageView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            imageView = viewHolder.imageView;
        }
        String imagePath = list.get(position).get("imagePath") + "";
        String addPhotoDrawable = R.drawable.add_photo + "";
        if (imagePath.equals(addPhotoDrawable) || imagePath == addPhotoDrawable) {
            imageView.setImageResource(R.drawable.add_photo);
        } else {
            ImageZipUtil imageZipUtil = new ImageZipUtil();
            Bitmap bitmap = imageZipUtil.getimage(imagePath);
            imageView.setImageBitmap(bitmap);
        }
        System.out.println("---imagePath---" + imagePath + "---size---" + list.size());
        convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, context.getResources().getDisplayMetrics().widthPixels / 4));
        return convertView;
    }

    public class ViewHolder{
        ImageView imageView;
        public ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
