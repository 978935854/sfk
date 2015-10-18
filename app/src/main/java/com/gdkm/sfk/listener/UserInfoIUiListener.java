package com.gdkm.sfk.listener;

import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/8/18.
 */
public class UserInfoIUiListener implements IUiListener{
    @Override
    public void onComplete(Object o) {
        doComplete((JSONObject) o);
    }

    public void doComplete(JSONObject jsonObject){
    }
    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
