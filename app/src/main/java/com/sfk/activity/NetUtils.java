package com.sfk.activity;

import android.util.Log;

import com.sfk.Constant.Constant;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by root on 5/16/15.
 */
public class NetUtils {
    private static final String TAG = "NetUtils";

    //使用post方式登录
    public static String loginOfPost(String username, String password, String url) {
        HttpURLConnection conn = null;
        String info = null;
        try {
            info = new JSONStringer().object().key("info").value(404).endObject().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            //创建一个URL对象
            URL mURL = new URL(url);
            //调用URL的openConnection()方法,获取HttpURLConnection对象
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("POST");// 设置请求方法为post
            conn.setReadTimeout(1000 * 5);// 设置读取超时为5秒
            conn.setConnectTimeout(1000 * 10);// 设置连接网络超时为10秒
            conn.setDoOutput(true);// 设置方法允许服务器输出内容
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //post请求参数
            StringBuilder buf = new StringBuilder();
            if (username.indexOf("@") > 0 ) {
                buf.append("customer.cemail=" + URLEncoder.encode(username,"UTF-8") + "&");
            } else {
                buf.append("customer.ctelnum=" + URLEncoder.encode(username,"UTF-8") + "&");
            }
            buf.append("customer.cpassword=" + URLEncoder.encode(password,"UTF-8"));
            byte[] data = buf.toString().getBytes("UTF-8");
            //获取一个输出流，向服务器写数据，默认情况下，系统不允许向服务器输出内容
            OutputStream out = conn.getOutputStream();// 获得一个输出流，向服务器写数据
            out.write(data);
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法

            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String state = getStringFromInputStream(is);
                return state;
            } else {
                return info;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return info;
    }

    //使用get方式登录
    public static String loginOfGet(String username, String password) {
        HttpURLConnection conn = null;
        String info = null;
        try {
            info = new JSONStringer().object().key("info").value(404).endObject().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = null;
        try {
            data = "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" +
                    URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = Constant.projectServicePath+"register?" + data;

        try{
            // 利用string url构建URL对象
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(1000 * 5);
            conn.setConnectTimeout(1000 * 10);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            Log.i("url",conn.getURL() + "");
            int responseCode = conn.getResponseCode();
            String state = null;
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                state = getStringFromInputStream(is);
                return state;
            } else {
                return info;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
//        return info;
        return null;
    }
    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len = -1;

        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();//把流中的数据转换成字符串，采用的编码是UTF-8（模拟器默认编码）
        os.close();
        return state;
    }
}
