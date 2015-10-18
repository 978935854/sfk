package com.gdkm.sfk.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ViewUtils {
    /**
     * 在ScrollView定位某个View的位置
     * @param scrollView
     * @param view
     */
    public static void scollTo(ScrollView scrollView,View view){
//        int y = (int) view.getY();
//        System.out.println("----y----" + y);
//        scrollView.smoothScrollTo(0, y);
        //滚动到原点
        scrollView.scrollTo(0, 0);
        scrollView.smoothScrollTo(0, 0);
    }

}
