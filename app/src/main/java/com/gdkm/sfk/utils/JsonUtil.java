package com.gdkm.sfk.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public static <T> T convertToObj(JSONObject jsonObject,Class<T> cla){
        if(jsonObject==null) return null;
        Field[] fb  =cla.getDeclaredFields();
        T t;
        try {
            t = cla.newInstance();
            for(int j=0;j<fb.length;j++){
                String fieldName = fb[j].getName();
                String fieldNameU=fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
                Log.i("fieldNameU",fieldNameU);
                Method method=cla.getMethod("set"+fieldNameU, fb[j].getType());
                method.invoke(t, jsonObject.get(fieldName));
            }
            return t;

        } catch (SecurityException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T getBean(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }

    public static <T> List<T> getBeanList(String jsonString, final Class<T> T) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
        }
        return list;
    }
}