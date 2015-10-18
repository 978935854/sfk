package com.gdkm.sfk.asynwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.gdkm.sfk.adapter.TopicListAdapter;
import com.gdkm.sfk.utils.BaseProtocolUtil;

/**
 * Created by zxw on 2015/6/8.
 */
public  class ImageAsyncTask extends AsyncTask<String,Void,String> {
    String photoPath;
    TopicListAdapter.ViewCache viewCache;
    private Bitmap bitmap=null;
    public ImageAsyncTask(TopicListAdapter.ViewCache viewCache) {
        viewCache=viewCache;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            photoPath = BaseProtocolUtil.showPhotoPath(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoPath;
    }

    @Override
    protected void onPostExecute(String s) {
        bitmap = BitmapFactory.decodeFile(s);
       // bbs_topicList.set_bbs_topicPhoto(bitmap);
    }
}