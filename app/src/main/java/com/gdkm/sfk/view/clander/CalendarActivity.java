package com.gdkm.sfk.view.clander;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdkm.sfk.R;

/**
 * 日历显示activity
 * 
 *
 */
public class CalendarActivity extends Activity implements OnGestureListener {
	private View view;
	private GestureDetector gestureDetector = null;
	private static CalendarAdapter calV = null;
	private static GridView gridView = null;
	private static TextView topText;
	private static TextView year_tv;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private static int year_c = 0;
	private static int month_c = 0;
	private static int day_c = 0;
	private String currentDate = "";
	private ImageButton btn_prev_month,btn_next_month,year_last_btn,year_next_btn;
	private Animation toLeft,toRight ;
	private static PopupWindowView pupupWindowView;
	private RelativeLayout calendarHead_show;
	private static Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cd_calendar);
		context = this;
		gestureDetector = new GestureDetector(this);
		initData();
		initView();
		today();	//跳转到今天
	}
	
	/*初始化数据*/
	private void initData() {
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);	
	}

	/*初始化界面*/
	private void initView() {
		calV = new CalendarAdapter(this,jumpMonth,jumpYear,year_c,month_c,day_c);
        addGridView();
        gridView.setAdapter(calV);
        gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tvtext = (TextView) arg1.findViewById(R.id.tvtext);
				int year = Integer.parseInt(year_tv.getText().toString().split("年")[0]);
				int month = Integer.parseInt(topText.getText().toString().split("月")[0]);
				int day = Integer.parseInt(tvtext.getText().toString());
				if(0<=arg2&&arg2<=7){
					if(23<=day&&day<=31){
						month-=1;
					}
				}else if(28<=arg2&&arg2<=41){
					if(1<=day&&day<=7){
						month+=1;
					}
				}else{
				}
				Toast.makeText(CalendarActivity.this, 
						year+"年"+month+"月"+day+"日",
						Toast.LENGTH_SHORT)
						.show();
			}
		});
        
        
		topText = (TextView) findViewById(R.id.tv_month);
		year_tv = (TextView) findViewById(R.id.year_tv);
		//上个月按钮
		btn_prev_month = (ImageButton) findViewById(R.id.btn_prev_month);
		btn_prev_month.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lastMonth();
			}
		});
		//下个月按钮
		btn_next_month = (ImageButton) findViewById(R.id.btn_next_month);
		btn_next_month.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextMonth();
			}
		});
		//下一年按钮
		year_next_btn = (ImageButton) findViewById(R.id.year_next_btn);
		year_next_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextYear();
			}
		});
		//上一年按钮
		year_last_btn = (ImageButton) findViewById(R.id.year_last_btn);
		year_last_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lastYear();
			}
		});
		
		//初始化PupupWindowView类
//		pupupWindowView = new PopupWindowView(CalendarActivity.this,alertDialog);
		calendarHead_show = (RelativeLayout) findViewById(R.id.calendarHead_show);
		year_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pupupWindowView.showYearPW(calendarHead_show);
			}
		});	
		topText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pupupWindowView.showMonthPW(calendarHead_show);
			}
		});	
		Button today_btn = (Button) findViewById(R.id.today_btn);
		today_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到今天
	        	today();
			}
		});
		//改变头部日期
		addTextToTopTextView(year_tv,topText);
	
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
            //像左滑动
			nextMonth();
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
			lastMonth();
			return true;
		}
		return false;
	}
	
	//上一个月操作
	public void lastMonth(){
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		jumpMonth--;     //上一个月
		toRight = AnimationUtils.loadAnimation(CalendarActivity.this, R.anim.cd_date_anm_to_right);
        gridView.setAnimation(toRight);
		calV = new CalendarAdapter(this,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(year_tv,topText);
        
        
	}
	
	//下一个月操作
	public void nextMonth(){
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		jumpMonth++;     //下一个月
		toLeft = AnimationUtils.loadAnimation(CalendarActivity.this, R.anim.cd_date_anm_to_left);
        gridView.setAnimation(toLeft);
		calV = new CalendarAdapter(this,jumpMonth,jumpYear,year_c,month_c,day_c);
		gridView.setAdapter(calV);
        addTextToTopTextView(year_tv,topText);
        gvFlag++;
        
        
	}
	
	//上一个年操作
	public void lastYear(){
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		jumpMonth-=12;     //上一个月
		toRight = AnimationUtils.loadAnimation(CalendarActivity.this, R.anim.cd_date_anm_to_right);
        gridView.setAnimation(toRight);
        
		calV = new CalendarAdapter(this,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(year_tv,topText);
        
	}
	
	//下一个年操作
	public void nextYear(){
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		jumpMonth+=12;     //下一个月
		toLeft = AnimationUtils.loadAnimation(CalendarActivity.this, R.anim.cd_date_anm_to_left);
        gridView.setAnimation(toLeft);
		calV = new CalendarAdapter(this,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(year_tv,topText);
        gvFlag++;
	}
	
	/*跳转到今天*/
	public void today(){
		int xMonth = jumpMonth;
    	int xYear = jumpYear;
    	int gvFlag =0;
    	jumpMonth = 0;
    	jumpYear = 0;
    	addGridView();   //添加一个gridView
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	calV = new CalendarAdapter(this,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(year_tv,topText);
        gvFlag++;
	}
	
	/*选择日期方法*/
	public static void selectDate(String selectText){
		int number = Integer.valueOf(selectText);
		String strTopText = topText.getText()+"";
		String[] aa = strTopText.split("月");
		if(number>12){
			String topYearText = year_tv.getText()+"";
			aa = topYearText.split("年");
			int number2 = number-(Integer.parseInt(aa[0]));
			if(number2>0){
				jumpMonth+=(number2*12);
			}else if(number2<0){
				number2 =- (number2*12);
				jumpMonth -= number2;
			}else{
				
			}
		}else{
			int number2 = number-(Integer.parseInt(aa[0]));
			if(number2>0){
				jumpMonth+=number2;
			}else if(number2<0){
				number2 = -number2;
				jumpMonth-=number2;
			}else{
				
			}
		}
		
		calV = new CalendarAdapter(context,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(year_tv,topText);
//        gvFlag++;
        pupupWindowView.dismissPW();
	}
	
	
	//添加头部的年份 闰哪月等信息
	public static void addTextToTopTextView(TextView year_tv,TextView month_tv){
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		year_tv.setText(calV.getShowYear()+"年");
		month_tv.setText(calV.getShowMonth()+"月");
		
		year_tv.setTextColor(Color.WHITE);
		year_tv.setTypeface(Typeface.DEFAULT_BOLD);
		
		month_tv.setTextColor(Color.WHITE);
		month_tv.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//添加gridview
	private void addGridView() {
		gridView =(GridView)findViewById(R.id.gridview);
		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return CalendarActivity.this.gestureDetector.onTouchEvent(event);
			}
		});           
	}
	
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "今天");
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 选择菜单
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case Menu.FIRST:
        	//跳转到今天
        	today();
        	break;
        }
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
}