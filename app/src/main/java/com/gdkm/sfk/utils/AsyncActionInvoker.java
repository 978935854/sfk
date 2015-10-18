package com.gdkm.sfk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.pojo.FormField;
import com.gdkm.sfk.pojo.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/** 使用方法：
 * 1、set请求方法；
 * 2、addField数据；
 * 3、submitMessage提交信息
 *
 * Created by Administrator on 2015/8/15.
 */
public class AsyncActionInvoker {
    private HttpURLConnection conn;
    private StringBuilder sb = new StringBuilder();
    private StringBuilder data = new StringBuilder();
    private String strData;
    private String path;
    private List<FormField> formFieldList;
    private String requestMethod;
    private FormField formField;
    private OnTextClickListener onTextClickListener;

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public AsyncActionInvoker() {
        path = Constant.projectServicePath;
        formFieldList = new ArrayList<FormField>();
    }

    /**
     * 提交信息
     */
    public String submitMessage(String actionName) {
        path=path+actionName;
        HttpRequestThread thread = new HttpRequestThread();
        thread.start();

        return null;
    }

    /**
     * 异步请求
     */
    class HttpRequestThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                if(requestMethod.equals("POST")){
                    packPost(path);
                }else if(requestMethod.equals("GET")){
                    processMessage();
                    path=path+"?"+strData;
                    packGet(path);
                }
                String text = parse();
                Message msg = new Message();
                msg.obj = text;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将返回来的数据放到UI线程
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String text = (String) msg.obj;
            onTextClickListener.returnMessage(text);
        }
    };

    /**
     * GET请求方式
     * @param url
     * @throws IOException
     */
    private void packGet(String url) throws IOException {
        Log.i("baseurl", url);
        conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(1000 * 5);
        conn.setConnectTimeout(1000 * 10);
        conn.setDoOutput(true);
        conn.setDoInput(true);
    }


    /**
     * POST请求方式
     */
    private void packPost(String url) throws IOException {
        Log.i("baseurl", url);
        processMessage();
        byte[] newData = strData.toString().getBytes();
        conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(newData.length));
        conn.setConnectTimeout(3000);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(newData);
        os.flush();
        os.close();
        System.out.println(strData);
    }

    /**
     * 将要提交的信息整合
     * @throws UnsupportedEncodingException
     */
    private void processMessage() throws UnsupportedEncodingException {
        for (FormField formField : formFieldList){
                data.append(formField.getName());
                data.append("=");
                data.append(URLEncoder.encode(formField.getValue().toString(), "UTF-8"));
                data.append("&");
        }
        if (data.length()>0){
            data.deleteCharAt(data.length()-1);
        }
        strData = data.toString();
    }

    /**
     *  服务器端返回数据并解析
     */
    private String parse()  {
        try {
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    sb.append(s);
                }
                Log.i("sb", sb.toString());
                is.close();
                bufferedReader.close();
            }else{

            }
        } catch (IOException e) {
            sb.append("error");
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 增加信息到服务器端
     * @param name
     * @param value
     */
    public void addField(String name,String value){
        formField = new FormField(name,value);
        formFieldList.add(formField);
    }

    /**
     * 获取服务器端返回来的JSONObject数据并解析
     * @param arrayKey
     * @return
     * @throws JSONException
     */
    public JSONArray getJSONArray(String arrayKey) throws JSONException {
        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONArray jsonArray = jsonObject.getJSONArray(arrayKey);
        return jsonArray;
    }

    /**
     * 展示并异步加载图片
     * @param path
     * @return
     * @throws Exception
     */
    public static String showPhotoPath(String path) throws Exception {
        String photoUrl = Constant.projectServicePath+path;
        HttpURLConnection conn = (HttpURLConnection) new URL(photoUrl).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        if(conn.getResponseCode()==200){
            Log.i("photoUrl",photoUrl);
            MD5 md5 = new MD5();
            File cache=null;
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                cache = new File(Environment.getExternalStorageDirectory(), "LvXingCache");
                if(!cache.exists()){
                    cache.mkdirs();
                }
            }else{
                Log.i("sdcardError","Not sdcard");
            }

            Log.i("cache1111",String.valueOf(cache));
            File cacheImage = null;
            if(path!=null){
                cacheImage = new File(cache,md5.getMD5(path)+path.substring(path.lastIndexOf(".")));
            }
            if(cacheImage.exists()){
                return cacheImage.getPath();
            }else{
                InputStream is = conn.getInputStream();
                FileOutputStream os = new FileOutputStream(cacheImage);
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = is.read(buffer))!=-1){
                    os.write(buffer,0,len);
                }
                os.flush();
                is.close();
                os.close();
                return cacheImage.getPath();
            }
        }
        return null;


    }

    /**
     * 展示并异步加载图片2
     * @param path
     * @return
     * @throws Exception
     */
    public static String TAG="UTIL11";
    public static Bitmap getbitmap(String imageUri) {
        Log.v(TAG, "getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v(TAG, "image download finished." + imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "getbitmap bmp fail---");
            return null;
        }
        return bitmap;
    }


    /**
     * 获取服务器端返回来的JSON数据并解析
     * @return
     * @throws JSONException
     */
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject(sb.toString());
        return jsonObject;
    }

    public interface OnTextClickListener{
        public void returnMessage(String text);
    }
}
