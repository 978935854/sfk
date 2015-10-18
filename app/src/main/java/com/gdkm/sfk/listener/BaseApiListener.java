package com.gdkm.sfk.listener;

import android.util.Log;

import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2015/8/18.
 */
public class BaseApiListener implements IRequestListener {
    public static final String Tag = "BaseApiListener";
    @Override
    public void onComplete(JSONObject jsonObject) {
       Log.i(Tag,"onComplete:"+jsonObject);
    }

    @Override
    public void onIOException(IOException e) {

    }

    @Override
    public void onMalformedURLException(MalformedURLException e) {

    }

    @Override
    public void onJSONException(JSONException e) {

    }

    @Override
    public void onConnectTimeoutException(ConnectTimeoutException e) {

    }

    @Override
    public void onSocketTimeoutException(SocketTimeoutException e) {

    }

    @Override
    public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {

    }

    @Override
    public void onHttpStatusException(HttpUtils.HttpStatusException e) {

    }

    @Override
    public void onUnknowException(Exception e) {

    }
}
