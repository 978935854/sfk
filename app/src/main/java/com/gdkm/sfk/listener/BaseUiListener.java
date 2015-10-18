package com.gdkm.sfk.listener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/8/18.
 */
public class BaseUiListener implements IUiListener {
    private Context context;
    public static final String Tag = "BaseUiListener";
    public BaseUiListener(Context context) {
        this.context = context;
    }

    @Override
    public void onComplete(Object o) {
        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
        doComplete((JSONObject) o);
    }

    /**
     * 处理返回的消息 比如把json转换为对象什么的
     *
     * @param values
     */
    protected void doComplete(JSONObject values) {
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(context, "登录取消", Toast.LENGTH_SHORT).show();
    }
}
