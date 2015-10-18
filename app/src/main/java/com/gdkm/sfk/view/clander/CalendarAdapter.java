package com.gdkm.sfk.view.clander;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdkm.sfk.R;

/**
 * 日历gridview中的每一个item显示的textview
 * @author lmw
 *
 */
public class CalendarAdapter extends BaseAdapter {
	private boolean isLeapyear = false;  //是否为闰年
	private int daysOfMonth = 0;      //某月的天数
	private int dayOfWeek = 0;        //具体某一天是星期几
	private int lastDaysOfMonth = 0;  //上一个月的总天数
	private Context context;

	private List<Integer> dayNumber = new ArrayList<Integer>(); //一个gridview中的日期存入此List中
	private SpecialCalendar sc = null;
	
	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1;     //用于标记当天
	private int[] schDateTagFlag = null;  //存储当月所有的日程日期
	private int startDay = -1;	//入住那天
	private int endDay = -1;		//离开那天

	private String showYear = "";   //用于在头部显示的年份
	private String showMonth = "";  //用于在头部显示的月份
	private String animalsYear = ""; 
	private String leapMonth = "";   //闰哪一个月
	private String cyclical = "";   //天干地支
	//系统当前时间
	private String sysDate = "";  
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	
	private Holder holder;
	public CalendarAdapter(){
		Date date = new Date();
		sysDate = sdf.format(date);  //当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
	}
	
	public CalendarAdapter(Context context,int jumpMonth,int jumpYear,int year_c,int month_c,int day_c){
		this();
		this.context= context;
		sc = new SpecialCalendar();
		int stepYear = year_c+jumpYear;
		int stepMonth = month_c+jumpMonth ;
		if(stepMonth > 0){
			//往下一个月滑动
			if(stepMonth%12 == 0){
				stepYear = year_c + stepMonth/12 -1;
				stepMonth = 12;
			}else{
				stepYear = year_c + stepMonth/12;
				stepMonth = stepMonth%12;
			}
		}else{
			//往上一个月滑动
			stepYear = year_c - 1 + stepMonth/12;
			stepMonth = stepMonth%12 + 12;
			if(stepMonth%12 == 0){
				
			}
		}
	
		currentYear = String.valueOf(stepYear);;  //得到当前的年份
		currentMonth = String.valueOf(stepMonth);  //得到本月 （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
		currentDay = String.valueOf(day_c);  //得到当前日期是哪天
		
		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
	}
	
	@Override
	public int getCount() {
		return dayNumber.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.cd_calendar_item, null);
			holder.textView = (TextView) convertView.findViewById(R.id.tvtext);
			holder.tvTips = (TextView) convertView.findViewById(R.id.tv_tips);
			holder.rlyCdItem = (RelativeLayout) convertView.findViewById(R.id.rly_cd_item);
			convertView.setTag(holder);
		 }else{
			 holder =  (Holder) convertView.getTag();
		 }
		String d = dayNumber.get(position)+"";
		holder.textView.setText(d);

		initItemStyle(convertView);
		setOnClickStyle(convertView, position, d);
		if ("离开".equals(holder.tvTips)){

		}
		return convertView;
	}

	/**
	 * 初始化item样式
	 * @param convertView
	 */
	private void initItemStyle(View convertView) {
		holder.rlyCdItem.setBackgroundResource(R.color.white);
		holder.textView.setTextColor(Color.GRAY);
		holder.tvTips.setText("");

		//设置item宽高一样
		int itemWidth = (context.getResources().getDisplayMetrics().widthPixels)/7-2;
		int itemHeight = itemWidth;
		AbsListView.LayoutParams param = new AbsListView.LayoutParams(
				itemWidth,
				itemHeight);
		convertView.setLayoutParams(param);
	}

	/**
	 * 设置item点击后的样式
	 * @param convertView
	 * @param position
	 * @param d
	 */
	private void setOnClickStyle(View convertView, int position, String d) {
		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			// 当前月信息显示
			holder.textView.setTextColor(Color.BLACK);// 当月字体设黑
		}
		if(schDateTagFlag != null && schDateTagFlag.length >0){
			for(int i = 0; i < schDateTagFlag.length; i++){
				if(schDateTagFlag[i] == position){
					//设置日程标记背景
					holder.textView.setBackgroundResource(R.drawable.cd_mark);
				}
			}
		}
		if(currentFlag == position){
			//设置当天的背景
			holder.rlyCdItem.setBackgroundResource(R.drawable.cd_circle_blue);
			holder.textView.setTextColor(Color.RED);
		}

//		System.out.println("startDay:"+startDay+",endDay:"+endDay);
		//开始到入住期间连续设置样式
		if(position>startDay && position<=endDay){
			setSelectorDayStyle(d);
			holder.tvTips.setText("入住");
		}
		//刚开始点击设置为入住
		if (position==startDay){
			setSelectorDayStyle(d);
			holder.tvTips.setText("入住");
		}

		//点击多次后，再点击重合同天，统一为入住
		if (startDay == endDay && startDay>=0){
			setSelectorDayStyle(d);
			holder.tvTips.setText("入住");
			endDay = -1;
		}
		//点击后设置样式为离开
		if (endDay==position){
			//当开始天数在入住前面，就设置为入住，清空离开项
			if (startDay>endDay && startDay>=0){
				setSelectorDayStyle(d);
				holder.tvTips.setText("入住");
				endDay = -1;
			}else{	//否则设置为离开
				setSelectorDayStyle(d);
				holder.tvTips.setText("离开");
			}
		}

		//当第二次点击以后，离开日子小于入住日子，就设置离开日子为入住日子，取消离开日子显示
		if (endDay<startDay && endDay>=0 &&startDay>=0){
			startDay = endDay;
			endDay = -1;
			}
		}

	/**
	 * 设置选中item的样式s
	 * @param day
	 */
	private void setSelectorDayStyle(String day) {
		holder.textView.setText(day);
		holder.textView.setTextColor(Color.WHITE);
		holder.tvTips.setTextColor(Color.WHITE);
		holder.rlyCdItem.setBackgroundResource(R.color.main_blue);
	}

	//得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month){
		isLeapyear = sc.isLeapYear(year);              //是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);  //某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month);      //某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month-1);  //上一个月的总天数
		Log.d("DAY", isLeapyear + " ======  " + daysOfMonth + "  ============  " + dayOfWeek + "  =========   " + lastDaysOfMonth);
		getweek(year, month);
	}
	
	//将一个月中的每一天的值添加入List dayNuber中
	private void getweek(int year, int month) {
		int j = 1;
		int i = 0;
		boolean action=true;
		while (action) {
			 if(i < dayOfWeek){  //前一个月
				int temp = lastDaysOfMonth - dayOfWeek+1;
				dayNumber.add((temp + i)) ;
			}else if(i < daysOfMonth + dayOfWeek){   //本月
				String day = String.valueOf(i-dayOfWeek+1);   //得到的日期
				dayNumber.add((i-dayOfWeek+1));
				//对于当前月才去标记当前日期
				if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)){
					//标记当前日期
					currentFlag = i;
				}	
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
			}else{   //下一个月
				dayNumber.add(j);
				if(i==34){
					int number = dayNumber.get(i);
					if(number>=1 && number<=7){
						action=false;
					}
				}else if(i==41){
					action=false;
				}
				j++;
			}
			 i++;
		}

	}
	
	
	public void matchScheduleDate(int year, int month, int day){
		
	}
	
	/**
	 * 点击每一个item时返回item中的日期
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position){
		return dayNumber.get(position)+"";
	}
	
	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * @return
	 */
	public int getStartPositon(){
		return dayOfWeek+7;
	}
	
	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * @return
	 */
	public int getEndPosition(){
		return  (dayOfWeek+daysOfMonth+7)-1;
	}
	
	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}
	
	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}
	
	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}
	
	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public int getStartDay() {
		return startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public List<Integer> getDayNumber() {
		return dayNumber;
	}

	public class Holder{
		TextView textView;
		TextView tvTips;
		RelativeLayout rlyCdItem;
	}
}
