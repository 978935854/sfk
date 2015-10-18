package com.gdkm.sfk.service;

import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.pojo.Sfk;
import com.gdkm.sfk.utils.BaseProtocolUtil;
import com.gdkm.sfk.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2015/6/8.
 */
public class SfInfoService {
    Sfk sfk = new Sfk();
    //根据沙发单ID，查询详细信息
    public Sfk findsfkById(final int sid) throws ExecutionException, InterruptedException {
        FutureTask<Sfk> getSfkInfo = new FutureTask<Sfk>(new Callable<Sfk>() {
            @Override
            public Sfk call() throws Exception {
                BaseProtocolUtil baseProtocolUtil = new BaseProtocolUtil();
                baseProtocolUtil.packGet(Constant.projectServicePath+"sfk/SfkAction!findsfkById?sfk.sid="+sid);
                baseProtocolUtil.parse();
                try {
                    JSONArray jsonArray = baseProtocolUtil.getJSONArray("sfkinfo");
                    sfk = JsonUtil.convertToObj((JSONObject)jsonArray.get(0),Sfk.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return sfk;
            }
        });
        new Thread(getSfkInfo).start();
        return getSfkInfo.get();
    }
}
