package com.sfk.service;

import com.sfk.Constant.Constant;
import com.sfk.pojo.Sfk;
import com.sfk.utils.BaseProtocolUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/27.
 */
public class SeekSFService {

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
            BaseProtocolUtil baseProtocolUtil = new BaseProtocolUtil();
            try {
//                baseProtocolUtil.packGet(Constant.projectServicePath+"SfkAction!findSeekSFTopicList");
//                baseProtocolUtil.parse();
                Map<String,String> map = new HashMap<String, String>();
                map.put("userName","黎明");
                map.put("userPass","liming");
                baseProtocolUtil.addDate(map);
                baseProtocolUtil.packPost(Constant.projectServicePath+"SfkAction!findSeekSFTopicList");
                baseProtocolUtil.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
