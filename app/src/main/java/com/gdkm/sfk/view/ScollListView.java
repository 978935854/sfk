package com.gdkm.sfk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/6/10.
 */
public class ScollListView extends ListView {
    public boolean isMeasure;
    public ScollListView(Context context) {
        super(context);
    }

    public ScollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
        Log.d("onMeasure", "onMeasure");
        isMeasure = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean isMeasure() {
        return isMeasure;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("onLayout", "onLayout");
        isMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }

}
