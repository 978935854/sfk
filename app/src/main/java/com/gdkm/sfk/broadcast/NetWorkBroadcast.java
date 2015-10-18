package com.gdkm.sfk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/6/14.
 */
public class NetWorkBroadcast extends BroadcastReceiver {
    Context context;
    public static final String TAG="NetState";
    @Override
    public void onReceive(Context context, Intent intent) {

//        Log.i(TAG,intent.getStringExtra("responseCode"));
        State wifiState = null;
        State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED == mobileState) {
            Log.i(TAG,"wifiState");
            // 手机网络连接成功
            Toast.makeText(context,"服务器端无响应，请稍后再试。",Toast.LENGTH_SHORT).show();
        } else if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED != mobileState) {
            Log.i(TAG,"NoALLState");
            Toast.makeText(context,"获取数据失败，请检查网络再重新加载。",Toast.LENGTH_SHORT).show();
            // 手机没有任何的网络
        } else if (wifiState != null && State.CONNECTED == wifiState) {
            Log.i(TAG,"handALLState");
//            Toast.makeText(context,"服务器端无响应，请稍后再试。",Toast.LENGTH_SHORT).show();
            // 无线网络连接成功
        }

    }
    /**
     * 对网络连接状态进行判断
     * @return  true, 可用； false， 不可用
     */
    private boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
