package com.gdkm.sfk.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2015/9/18.
 */
public class SetGridViewHeight {
    public static void setGridViewHeightBasedOnChildren(GridView gridView,Context context) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns=0;
        int horizontalBorderHeight=0;
        Class<?> clazz=gridView.getClass();
        try {
            //利用反射，取得每行显示的个数
            columns = gridView.getNumColumns();

            //利用反射，取得横向分割线高度
            Field horizontalSpacing=clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight=(Integer)horizontalSpacing.get(gridView);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if(listAdapter.getCount()%columns>0){
            rows=listAdapter.getCount()/columns+1;
        }else {
            rows=listAdapter.getCount()/columns;
        }
        int totalHeight = 0;
        int itemHeight = 0;
        columns = Math.abs(columns);
        if (columns==1){
            itemHeight = context.getResources().getDisplayMetrics().widthPixels/4;
        }else{
            itemHeight = context.getResources().getDisplayMetrics().widthPixels/columns;
        }
        totalHeight = Math.abs(itemHeight *rows);
        System.out.println("totalHeight:"+totalHeight);
//        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
//            View listItem = listAdapter.getView(i, null, gridView);
//            listItem.measure(0, 0); // 计算子项View 的宽高
//            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
//        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight+horizontalBorderHeight*(rows-1);//最后加上分割线总高度
        gridView.setLayoutParams(params);
    }
}
