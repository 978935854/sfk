package com.gdkm.sfk.utils;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

import com.gdkm.sfk.R;

/**
 * Created by Administrator on 2015/9/29.
 */
public class ScrollViewUtils {
    /**
     * ScrollView滚动到指定View的位置
     * @param view
     */
    public static void srcollTo(Context context,ScrollView sc,View view) {
        int Y = (int) ((View) view.getParent()).getY();
        sc.scrollTo(0, Y);
        view.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.share_ed_red_01));
    }
}
