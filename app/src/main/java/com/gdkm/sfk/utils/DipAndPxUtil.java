package com.gdkm.sfk.utils;

import android.content.Context;


/**   
*    
* 项目名称：
* 类名称：DipAndPxUtils
* 类描述： dp和px转换工具类  
* 创建人：lqz   
* 创建时间：    
*/
public class DipAndPxUtil {
	/** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    } 
}
