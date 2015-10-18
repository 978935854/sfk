package com.gdkm.sfk.service;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.pojo.Sfk;
import com.gdkm.sfk.utils.BaseProtocolUtil;
import com.gdkm.sfk.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/5/27.
 */
public class SeekSFService {
    public SeekSFService(Context context) {
        this.context = context;
    }

    private Context context;
    BaseProtocolUtil baseProtocolUtil;
    List<Sfk> seekSFTopicList;


    //获取沙发单列表
    public List<Sfk> getSeekSFTopicList(final int tid,int startPosition,int endPosition) throws InterruptedException, ExecutionException {
        seekSFTopicList = new ArrayList<Sfk>();
        baseProtocolUtil = new BaseProtocolUtil();
        try {
            baseProtocolUtil.packGet(Constant.projectServicePath+"sfk/SfkAction!findSeekSFTopicList?tid="+tid+"&startPosition="+startPosition+"&endPosition="+endPosition);
            String responseCode = baseProtocolUtil.parse();
            if("error".equals(responseCode)){
                Intent intent = new Intent();
                intent.setAction("isNetWork");
                intent.putExtra("responseCode",responseCode);
                context.sendBroadcast(intent);
            }

            JSONArray sfkArray = baseProtocolUtil.getJSONArray("sfkArray");
            for(int i=0;i<sfkArray.length();i++){
                Sfk sfk = JsonUtil.convertToObj((org.json.JSONObject) sfkArray.get(i),Sfk.class);
                seekSFTopicList.add(sfk);
            }
            Log.i("sfkList",seekSFTopicList.toString());
            return seekSFTopicList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return seekSFTopicList;

    }


    //筛选条件，获取沙发单
    public List<Sfk> getSeekSFTopicListBySfk(final Sfk sfk) throws InterruptedException, ExecutionException {
           seekSFTopicList = new ArrayList<Sfk>();
           baseProtocolUtil = new BaseProtocolUtil();
           try {
                String saddress = URLEncoder.encode(sfk.getSaddress(),"UTF-8");
                String ssex = URLEncoder.encode(sfk.getSsex(),"UTF-8");
                Log.i("saddress22",ssex+"");
                 baseProtocolUtil.packGet(Constant.projectServicePath+"sfk/SfkAction!findSeekSFTopicListBySfk?"
                                        +"sfk.ssex="+ssex+"&"
                                        +"sfk.saddress="+saddress+"&"
                                        +"sfk.speoplenum="+sfk.getSpeoplenum()+"&"
                                        +"sfk.tid="+sfk.getTid()+"&"
                                         +"sfk.startPosition="+sfk.getStartPosition()+"&"
                                         +"sfk.endPosition="+sfk.getEndPosition()
                        );
                        String responseCode = baseProtocolUtil.parse();
                        if("error".equals(responseCode)){
                            Intent intent = new Intent();
                            intent.setAction("isNetWork");
                            intent.putExtra("responseCode",responseCode);
                            context.sendBroadcast(intent);
                        }
                        JSONArray sfkArray = baseProtocolUtil.getJSONArray("sfkArray");
                        for(int i=0;i<sfkArray.length();i++){
                            Sfk sfk2 = JsonUtil.convertToObj((org.json.JSONObject) sfkArray.get(i),Sfk.class);
                            seekSFTopicList.add(sfk2);
                        }
                    return seekSFTopicList;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               }

        return seekSFTopicList;
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
