package com.gdkm.sfk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
            new NewsAsyncTask(imageView,url).execute(url);
        }else {
            imageView.setImageBitmap(bitmap);
        }

    }

    public class NewsAsyncTask extends AsyncTask<String,Void,Bitmap>{
        private ImageView imageView;
        private String murl;
        private  NewsAsyncTask(ImageView imageView, String url){
            this.imageView = imageView;
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
            if (imageView.getTag()==murl){
                imageView.setImageBitmap(bitmap);
            }
//            ImageView imageView = (ImageView)mListview.findViewWithTag(murl);
//            if(imageView!=null&& bitmap!=null){
//                imageView.setImageBitmap(bitmap);
//            }
        }
    }


}
