package com.gdkm.sfk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdkm.sfk.R;
import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.pojo.Topic;
import com.gdkm.sfk.utils.BaseProtocolUtil;
import com.gdkm.sfk.utils.DipAndPxUtil;
import com.gdkm.sfk.view.ImageViewDialog;
import com.gdkm.sfk.view.ScollListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxw on 2015/6/4.
 */
public class TopicListAdapter extends BaseAdapter {
    private Context context;
    private List<Topic> topicList;
    private LayoutInflater inflater;
    private ImageViewDialog imageViewDialog ;
    public TopicListAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageViewDialog = new ImageViewDialog(context);
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
        TextView topicTime,topicUserName,topicTitle,topicContent;
        LinearLayout llyBbsPhoto;
        if (parent instanceof ScollListView){
            if (((ScollListView)parent).isMeasure()){
                convertView = inflater.inflate(R.layout.bbs_list_text,null);
                topicTime=(TextView)convertView.findViewById(R.id.bbs_topicTime);
                topicTitle=(TextView)convertView.findViewById(R.id.bbs_topicTitle);
                topicUserName=(TextView)convertView.findViewById(R.id.bbs_userName);
                topicContent=(TextView)convertView.findViewById(R.id.bbs_topic_content);
                llyBbsPhoto = (LinearLayout) convertView.findViewById(R.id.lly_bbsPhoto);
                ViewCache viewCache=new ViewCache();
                viewCache.topicTime=topicTime;
                viewCache.topicTitle=topicTitle;
                viewCache.topicUserName=topicUserName;
                viewCache.topicContent=topicContent;
                viewCache.llyBbsPhoto =llyBbsPhoto;
                convertView.setTag(viewCache);
            }else{
                convertView = initDapterView(convertView,position);
            }
        }
        return convertView;
    }

    private View initDapterView(View convertView,int position) {
        TextView topicTime,topicUserName,topicTitle,topicContent;
        LinearLayout llyBbsPhoto;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.bbs_list_text,null);
            topicTime=(TextView)convertView.findViewById(R.id.bbs_topicTime);
            topicTitle=(TextView)convertView.findViewById(R.id.bbs_topicTitle);
            topicUserName=(TextView)convertView.findViewById(R.id.bbs_userName);
            topicContent=(TextView)convertView.findViewById(R.id.bbs_topic_content);
            llyBbsPhoto = (LinearLayout) convertView.findViewById(R.id.lly_bbsPhoto);
            ViewCache viewCache=new ViewCache();
            viewCache.topicTime=topicTime;
            viewCache.topicTitle=topicTitle;
            viewCache.topicUserName=topicUserName;
            viewCache.topicContent=topicContent;
            viewCache.llyBbsPhoto =llyBbsPhoto;
            convertView.setTag(viewCache);
        }else{
            ViewCache viewCache=(ViewCache)convertView.getTag();
            topicTime=viewCache.topicTime;
            topicTitle=viewCache.topicTitle;
            topicUserName=viewCache.topicUserName;
            topicContent=viewCache.topicContent;
            llyBbsPhoto = viewCache.llyBbsPhoto;
        }
        Topic topic= topicList.get(position);
        //下面代码实现数据绑定
        topicTime.setText(topic.getTreleasetime());
        topicTitle.setText(topic.getTopictitle());
        setUserName(topicUserName,topic);
        topicContent.setText(topic.getTopiccontent());
        System.out.println("---size-"+topicList.size());
        addTopicPhoto(llyBbsPhoto, topic.getPhotoList());
        Log.i("time1",topic.getTreleasetime());
        return convertView;
    }

    /**
     * 设置用户名
     * @param topicUserName
     * @param topic
     */
    private void setUserName(TextView topicUserName, Topic topic) {
        System.out.println("----getCcusername--"+topic.getCcusername());
        if (null==topic.getCcusername()||"".equals(topic.getCcusername())){
            if (null==topic.getCemail()||"".equals(topic.getCemail())){
                topicUserName.setText(topic.getCtelnum());
            }else{
                topicUserName.setText(topic.getCemail());
            }
        }else {
            topicUserName.setText(topic.getCcusername());
        }
    }

    /**
     * 动态新增imagebutton到帖子
     * @param llyBbsPhoto
     * @param photoList
     */
    private void addTopicPhoto(LinearLayout llyBbsPhoto, List<String> photoList) {
        for (int i=0;i<photoList.size();i++){
            ImageButton imageButton = new ImageButton(context);
            imageButton.setBackgroundDrawable(new BitmapDrawable());
            imageButton.setTag(photoList.get(i));
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(DipAndPxUtil.dip2px(context,60),DipAndPxUtil.dip2px(context,60));
            layoutParams.setMargins(10,5,0,5);
            imageButton.setLayoutParams(layoutParams);
            ImageAsyncTask imageAsyncTask = new ImageAsyncTask(imageButton);
            imageAsyncTask.execute(photoList.get(i));
            llyBbsPhoto.addView(imageButton);
        }
    }

    class ImgBtnListener implements View.OnClickListener{
        private String path;
        public ImgBtnListener(String path) {
            this.path = path;
        }

        @Override
        public void onClick(View v) {
            imageViewDialog.show();
            imageViewDialog.setImage(path);
        }
    }

    class ImageAsyncTask extends AsyncTask<String,Void,Map<String,Object>>{
        private ImageButton imageButton;

        public ImageAsyncTask(ImageButton imageButton) {
            this.imageButton = imageButton;
        }

        @Override
        protected Map<String,Object> doInBackground(String... params) {
            Map<String,Object> map = new HashMap<String, Object>();
            Bitmap bitmap = null;
            String path = null;
            try {
                path = BaseProtocolUtil.showPhotoPath(params[0]);
                System.out.println("----path----"+path);
                bitmap = BitmapFactory.decodeFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("bitmap",bitmap);
            map.put("path",path);
            return map;
        }

        @Override
        protected void onPostExecute(Map<String,Object> map) {
            super.onPostExecute(map);
            Bitmap bitmap = zoomImage((Bitmap) map.get("bitmap"),DipAndPxUtil.dip2px(context,60),DipAndPxUtil.dip2px(context,60));
            imageButton.setImageBitmap(bitmap);
            imageButton.setOnClickListener(new ImgBtnListener(map.get("path")+""));
        }
    }

    public static class ViewCache{
        TextView topicTime,topicUserName,topicTitle,topicContent;
        ImageView imageView;
        LinearLayout llyBbsPhoto;
    }

    public Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        Bitmap bitmap = null;
        // 获取这个图片的宽和高
        if (null!=bgimage){
            float width = bgimage.getWidth();
            float height = bgimage.getHeight();
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 缩放图片动作
            matrix.postScale(scaleWidth, scaleHeight);
            bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                    (int) height, matrix, true);
        }
        return bitmap;
    }
}
