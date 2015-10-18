package com.gdkm.sfk.utils;

import android.os.Environment;
import android.util.Log;

import com.gdkm.sfk.constant.Constant;

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
import java.util.Map;

/**
 * Created by Administrator on 2015/5/27.
 */
public class BaseProtocolUtil {
    private HttpURLConnection conn;
    private StringBuilder sb = new StringBuilder();
    private StringBuilder data = new StringBuilder();
    private String strData;
    //GET请求方式
    public void packGet(String url) {
        Log.i("baseurl",url);
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(1000 * 5);
            conn.setConnectTimeout(1000 * 10);
            conn.setDoOutput(true);
            conn.setDoInput(true);
        } catch (IOException e) {
            e.printStackTrace();
    }

    }


    //POST请求方式
    public void packPost(String url){
        Log.i("baseurl",url);
        byte[] newData = strData.toString().getBytes();
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",String.valueOf(newData.length));
            conn.setConnectTimeout(3000);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(newData);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //服务器端返回数据并解析
    public String parse()  {
        String responseCode = null;
        try {
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    sb.append(s);
                }
                Log.i("sb",sb.toString());
                is.close();
                bufferedReader.close();
                responseCode="ok";
            }else{

            }
        return responseCode;
        } catch (IOException e) {
            responseCode="error";
            e.printStackTrace();
            return responseCode;
        }

    }

    //添加数据到POST请求传输
    public void addDate(Map<String,String> map) throws UnsupportedEncodingException {
        for (Map.Entry<String,String> entry :map.entrySet()){
            data.append(entry.getKey());
            data.append("=");
            data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            data.append("&");
        }
        data.deleteCharAt(data.length()-1);
        strData = data.toString();
    }

    //获取服务器端返回来的JSONObject数据并解析
    public JSONArray getJSONArray(String arrayKey) throws JSONException {
        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONArray jsonArray = jsonObject.getJSONArray(arrayKey);
        return jsonArray;
    }
    public static String showPhotoPath(String path) throws Exception {
        String photoUrl = Constant.projectServicePath+path;
        System.out.println("----photoUrl---"+photoUrl);
        Log.i("photoUrl",photoUrl);
        HttpURLConnection conn = (HttpURLConnection) new URL(photoUrl).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        if(conn.getResponseCode()==200){
            MD5 md5 = new MD5();
            File cache=null;
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                cache = new File(Environment.getExternalStorageDirectory(), "sfkPhoto");
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
                is.close();
                os.close();
                return cacheImage.getPath();
            }
        }
        return null;


    }

    //获取服务器端返回来的JSON数据并解析
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject(sb.toString());
        return jsonObject;
    }

}
