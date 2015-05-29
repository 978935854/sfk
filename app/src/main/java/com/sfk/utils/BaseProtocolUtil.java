package com.sfk.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    public void packGet(String url) throws IOException {
        Log.i("baseurl",url);
        conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
    }


    //POST请求方式
    public void packPost(String url) throws IOException {
        Log.i("baseurl",url);
        byte[] newData = strData.toString().getBytes();
        conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",String.valueOf(newData.length));
        conn.setConnectTimeout(3000);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(newData);
        os.close();

    }


    public void parse() throws IOException {
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
        }
    }

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


}
