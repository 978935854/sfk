package com.sfk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.sfk.activity.R;
import com.sfk.adapter.SfinfoAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/6/8.
 */
public class ImageLoader {

    private LruCache<String,Bitmap> mCache;
    private ScollListview mListview;


    public ImageLoader(ScollListview listView){

        mListview=listView;
        int maxMemory =(int) Runtime.getRuntime().maxMemory();
        int cachesize=maxMemory/4;
        mCache =new LruCache<String,Bitmap>(cachesize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();

            }
        };
    }

    public void addBitmapTocache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url)==null){
            mCache.put(url,bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String url){
        return  mCache.get(url);

    }


    public Bitmap getBitmapFromURL(String urlString){
        Bitmap bitmap=null;
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            is= new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            }catch (Exception e){

            }
        }
        return  bitmap;
    }

    public void showImageviewAsyncTask(ImageView imageView,String url){


        Bitmap bitmap=getBitmapFromCache(url);

        if(bitmap==null){
            new NewsAsyncTask(url).execute(url);
        }else {
            imageView.setImageBitmap(bitmap);
        }

    }

    public class NewsAsyncTask extends AsyncTask<String,Void,Bitmap>{

        private String murl;
        private  NewsAsyncTask(String url){
            murl=url;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String url=params[0];
            Bitmap bitmap=getBitmapFromURL(url);
            if(bitmap!=null){
                addBitmapTocache(url,bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView)mListview.findViewWithTag(murl);
            if(imageView!=null&& bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }


}
