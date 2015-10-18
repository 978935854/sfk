package com.gdkm.sfk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomUtil {
	
	/**
	 * 生成时间加随机数
	 * @return
	 */
	public static String RandomNumber(){
		Date dt = new Date();
	   SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMddhhmmss");
	   String time = dtf.format(dt);//获取当地时间
	   Random r = new Random();
	   int rannum = (int) (r.nextDouble() * (99999 - 10000 + 1)) + 10000; // 获取随机数
	   return time+rannum;
	}
}
