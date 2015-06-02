package com.sfk.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.sfk.Constant.Constant;
import com.sfk.pojo.Sfk;
import com.sfk.utils.BaseProtocolUtil;
import com.sfk.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/27.
 */
public class SeekSFService {
    public SeekSFService(Context context) {
        this.context = context;
    }

    private Context context;
    BaseProtocolUtil baseProtocolUtil;
    //获取沙发单列表
    public List<Sfk> getSeekSFTopicList() throws InterruptedException {
        GetSeekSFTopicListThread topicListThread  = new GetSeekSFTopicListThread();
        topicListThread.start();
        topicListThread.join();
        return topicListThread.seekSFTopicList;
    }
    //获取所有沙发单
    private class GetSeekSFTopicListThread extends Thread{
        List<Sfk> seekSFTopicList;
        public void run(){
            seekSFTopicList = new ArrayList<Sfk>();
            baseProtocolUtil = new BaseProtocolUtil();
            try {
                baseProtocolUtil.packGet(Constant.projectServicePath+"sfk/SfkAction!findSeekSFTopicList");
                String responseCode = baseProtocolUtil.parse();
                if("error".equals(responseCode)){
                    Message msg = new Message();
                    boolean netState = isOpenNetwork(); //  检测网络是否正常
                    if(netState){       //本地网络正常，证明服务器有问题
                        msg.obj="serviceError";
                    }else{              //服务器端正常，证明本地网络有问题
                        msg.obj="locationError";
                    }
                    handler.sendMessage(msg);
                }

                JSONArray sfkArray = baseProtocolUtil.getJSONArray("sfkArray");
                for(int i=0;i<sfkArray.length();i++){
                    Sfk sfk = JsonUtil.convertToObj((org.json.JSONObject) sfkArray.get(i),Sfk.class);
                    seekSFTopicList.add(sfk);
                }
                Log.i("sfkList",seekSFTopicList.toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //筛选条件，获取沙发单
    public List<Sfk> getSeekSFTopicListBySfk(Sfk sfk) throws InterruptedException {
        GetSeekSFTopicListBySfkThread topicListThread  = new GetSeekSFTopicListBySfkThread(sfk);
        topicListThread.start();
        topicListThread.join();
        return topicListThread.seekSFTopicList;
    }

    //筛选条件，获取沙发单
    private class GetSeekSFTopicListBySfkThread extends Thread{
        Sfk sfk;
        List<Sfk> seekSFTopicList;
        public GetSeekSFTopicListBySfkThread(Sfk sfk) {
            this.sfk = sfk;
        }
        public void run(){
            seekSFTopicList = new ArrayList<Sfk>();
            baseProtocolUtil = new BaseProtocolUtil();
            try {
                baseProtocolUtil.packGet(Constant.projectServicePath+"sfk/SfkAction!findSeekSFTopicListBySfk?"
                        +"sfk.ssex="+sfk.getSsex()+"&"
                        +"sfk.saddress="+sfk.getSaddress()+"&"
                        +"sfk.speoplenum="+sfk.getSpeoplenum()
                );
                String responseCode = baseProtocolUtil.parse();
                if("error".equals(responseCode)){
                    Message msg = new Message();
                    boolean netState = isOpenNetwork(); //  检测网络是否正常
                    if(netState){       //本地网络正常，证明服务器有问题
                        msg.obj="serviceError";
                    }else{              //服务器端正常，证明本地网络有问题
                        msg.obj="locationError";
                    }
                    handler.sendMessage(msg);
                }
                JSONArray sfkArray = baseProtocolUtil.getJSONArray("sfkArray");
                for(int i=0;i<sfkArray.length();i++){
                    Sfk sfk = JsonUtil.convertToObj((org.json.JSONObject) sfkArray.get(i),Sfk.class);
                    seekSFTopicList.add(sfk);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //连接错误的提示信息UI线程
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String responseCode = (String) msg.obj;
            if("serviceError".equals(responseCode)){
                Toast.makeText(context,"服务器端无响应，请稍后再试。",Toast.LENGTH_SHORT).show();
            }else if ("locationError".equals(responseCode)){
                Toast.makeText(context,"获取数据失败，请检查网络再重新加载。",Toast.LENGTH_SHORT).show();
            }
            Log.i("responseCode",responseCode);
        }
    };


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
