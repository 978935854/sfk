package com.sfk.service;

import android.util.Log;

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
    BaseProtocolUtil baseProtocolUtil;
    //获取沙发单列表
    public List<Sfk> getSeekSFTopicList() throws InterruptedException {
        GetSeekSFTopicListThread topicListThread  = new GetSeekSFTopicListThread();
        topicListThread.start();
        topicListThread.join();
        return topicListThread.seekSFTopicList;
    }

    private class GetSeekSFTopicListThread extends Thread{
        List<Sfk> seekSFTopicList;
        public void run(){
            seekSFTopicList = new ArrayList<Sfk>();
            baseProtocolUtil = new BaseProtocolUtil();
            try {
                baseProtocolUtil.packGet(Constant.projectServicePath+"sfk/SfkAction!findSeekSFTopicList");
                baseProtocolUtil.parse();
                JSONArray sfkArray = baseProtocolUtil.getJSONArray("sfkArray");
                for(int i=0;i<sfkArray.length();i++){
                    Sfk sfk = JsonUtil.convertToObj((org.json.JSONObject) sfkArray.get(i),Sfk.class);
                    seekSFTopicList.add(sfk);
                }
                Log.i("sfkList",seekSFTopicList.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //根据性别获取沙发单列表
    public List<Sfk> getSeekSFTopicListBySfk(Sfk sfk) throws InterruptedException {
        GetSeekSFTopicListBySfkThread topicListThread  = new GetSeekSFTopicListBySfkThread(sfk);
        topicListThread.start();
        topicListThread.join();
        return topicListThread.seekSFTopicList;
    }

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
                baseProtocolUtil.parse();
                JSONArray sfkArray = baseProtocolUtil.getJSONArray("sfkArray");
                for(int i=0;i<sfkArray.length();i++){
                    Sfk sfk = JsonUtil.convertToObj((org.json.JSONObject) sfkArray.get(i),Sfk.class);
                    seekSFTopicList.add(sfk);
                }
                Log.i("sfkList",seekSFTopicList.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
