package com.gdkm.sfk.view.clander;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdkm.sfk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/22.
 */
public class CalendarDialog extends AlertDialog implements GestureDetector.OnGestureListener {
    private static Context context;
    private View view;
    private AlertDialog dialog;
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
    private String currentDateTwo = "";
    private int checkNumber = 0;
    private ImageButton btn_prev_month,btn_next_month,year_last_btn,year_next_btn;
    private Animation toLeft,toRight ;
    private static PopupWindowView pupupWindowView;
    private RelativeLayout calendarHead_show;
    private Button btnConfirm;
    private OnCalendarListener onCalendarListener;
    public void setOnCalendarListener(OnCalendarListener onCalendarListener) {
        this.onCalendarListener = onCalendarListener;
    }

    protected CalendarDialog(Context context) {
        super(context);
        this.context = context;
        gestureDetector = new GestureDetector(this);
    }

    protected CalendarDialog(Context context, int theme) {
        super(context, theme);
        gestureDetector = new GestureDetector(this);
    }

    protected CalendarDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        gestureDetector = new GestureDetector(this);
    }

    /*初始化数据*/
    private void initData() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

        SimpleDateFormat sdfTwo = new SimpleDateFormat("yyyy-MM-dd");
        currentDateTwo = sdfTwo.format(date);  //当期日期
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.cd_calendar, null);
        setContentView(dialogView);
        Window window = this.getWindow();
        window.setBackgroundDrawableResource(R.drawable.coner_picker);
        initData();
        initView();
        today();	//跳转到今天

    }

    /*初始化界面*/
    private void initView() {
        TextView tvNewTime = (TextView) findViewById(R.id.tv_newTime);
        tvNewTime.setText(currentDateTwo);

        calV = new CalendarAdapter(context,jumpMonth,jumpYear,year_c,month_c,day_c);
        addGridView();
        gridView.setAdapter(calV);
        gridView.setOnItemClickListener(new CalendarItemListener());

        topText = (TextView) findViewById(R.id.tv_month);
        year_tv = (TextView) findViewById(R.id.year_tv);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new MClick());
        //上个月按钮
        btn_prev_month = (ImageButton) findViewById(R.id.btn_prev_month);
        btn_prev_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastMonth();
            }
        });
        //下个月按钮
        btn_next_month = (ImageButton) findViewById(R.id.btn_next_month);
        btn_next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }
        });
        //下一年按钮
        year_next_btn = (ImageButton) findViewById(R.id.year_next_btn);
        year_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextYear();
            }
        });
        //上一年按钮
        year_last_btn = (ImageButton) findViewById(R.id.year_last_btn);
        year_last_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastYear();
            }
        });

        //初始化PupupWindowView类
        pupupWindowView = new PopupWindowView(context,this);
        calendarHead_show = (RelativeLayout) findViewById(R.id.calendarHead_show);
        year_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pupupWindowView.showYearPW(calendarHead_show);
            }
        });
        topText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pupupWindowView.showMonthPW(calendarHead_show);
            }
        });
        Button today_btn = (Button) findViewById(R.id.today_btn);
        today_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到今天
                today();
            }
        });
        //改变头部日期
        addTextToTopTextView(year_tv,topText);

    }


    class CalendarItemListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tvText = (TextView) view.findViewById(R.id.tvtext);
            TextView tvTips = (TextView) view.findViewById(R.id.tv_tips);
            int year = Integer.parseInt(year_tv.getText().toString().split("年")[0]);
            int month = Integer.parseInt(topText.getText().toString().split("月")[0]);
            int day = Integer.parseInt(tvText.getText().toString());
            String tips = tvTips.getText()+"";
            System.out.println("---tvTips---"+tips);
            if (calV.getStartDay()>-1){
                if ("离开".equals(tips) || "入住".equals(tips)){
                    calV.setStartDay(position);
                    calV.setEndDay(-1);
                }else {
                    calV.setEndDay(position);
                }
            }else{
                calV.setStartDay(position);
            }
            List<Integer> list = calV.getDayNumber();
            int startDay = -1;
            int endDay = -1;
            if (calV.getStartDay()>=0){
                startDay = list.get(calV.getStartDay());
            }
            if(calV.getEndDay()>=0){
                endDay = list.get(calV.getEndDay());
            }
            int startMonth = 0;
            int endMonth = 0;
            startMonth = calculateMonth(startDay,month,calV.getStartDay());
            endMonth = calculateMonth(endDay,month,calV.getEndDay());
            System.out.println("----item---startDay:" + startDay + ",endDay" + endDay);
            if (startDay==-1){
                startMonth = endMonth;
            }else if (endDay==-1){
                endDay = startDay;
                endMonth = startMonth;
            }
            calV.notifyDataSetChanged();
            checkNumber = calV.getEndDay()-calV.getStartDay();
            if(checkNumber<0){
                checkNumber=0;
            }
            checkNumber =checkNumber+1;
//            System.out.println("----item--checkNumber:"+(calV.getEndDay()-calV.getStartDay()));
//            System.out.println("----item---startYear:"+year+",startMonth:" + startMonth + ",startDay" + startDay);
//            System.out.println("----item---endYear:" + year + ",endMonth:" + endMonth + ",endDay" + endDay);

            if (checkNumber>1){
                dismiss();
            }
            String startTime = statisticsTime(year,startMonth,startDay);
            String endTime = statisticsTime(year,endMonth,endDay);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("startTime",startTime);
            map.put("endTime",endTime);
            map.put("checkNumber",checkNumber);
            onCalendarListener.OnCalendarItemListener(map);
//                dismiss();
        }
    }

    /**
     * 统计时间
     * @return
     */
    private String statisticsTime(int year,int month,int day){
        String strMonth = "";
        String strDay = "";
        if(month<10){
            strMonth = "0"+String.valueOf(month);
        }else{
            strMonth = month+"";
        }
        if (day<10){
            strDay = "0"+String.valueOf(day);
        }else{
            strDay = day+"";
        }
        String time = year+"-"+strMonth+"-"+strDay;
        return time;
    }

    /**
     * 计算天数
     */
    private int calculateMonth(int day,int month, int position){
        if(0<=position&&position<=7){
            if(23<=day&&day<=31){
                month-=1;
            }
        }else if(28<=position&&position<=41){
            if(1<=day&&day<=7){
                month+=1;
            }
        }else{
        }
        return month;
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
        toRight = AnimationUtils.loadAnimation(context, R.anim.cd_date_anm_to_right);
        gridView.setAnimation(toRight);
        calV = new CalendarAdapter(context,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(year_tv, topText);


    }

    //下一个月操作
    public void nextMonth(){
        int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
        jumpMonth++;     //下一个月
        toLeft = AnimationUtils.loadAnimation(context, R.anim.cd_date_anm_to_left);
        gridView.setAnimation(toLeft);
        calV = new CalendarAdapter(context,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(year_tv, topText);
        gvFlag++;


    }

    //上一个年操作
    public void lastYear(){
        int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
        jumpMonth-=12;     //上一个月
        toRight = AnimationUtils.loadAnimation(context, R.anim.cd_date_anm_to_right);
        gridView.setAnimation(toRight);

        calV = new CalendarAdapter(context,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(year_tv, topText);

    }

    //下一个年操作
    public void nextYear(){
        int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
        jumpMonth+=12;     //下一个月
        toLeft = AnimationUtils.loadAnimation(context, R.anim.cd_date_anm_to_left);
        gridView.setAnimation(toLeft);
        calV = new CalendarAdapter(context,jumpMonth,jumpYear,year_c,month_c,day_c);
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
        calV = new CalendarAdapter(context,jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(year_tv,topText);
        gvFlag++;
    }

    /*选择日期方法*/
    public static void selectDate(String selectText){
        int number = Integer.valueOf(selectText);
        String strTopText = topText.getText().toString();
        String[] aa = strTopText.split("月");
        if(number>12){
            String topYearText = year_tv.getText().toString();
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
        month_tv.setText(calV.getShowMonth() + "月");

        year_tv.setTextColor(Color.WHITE);
        year_tv.setTypeface(Typeface.DEFAULT_BOLD);

        month_tv.setTextColor(Color.WHITE);
        month_tv.setTypeface(Typeface.DEFAULT_BOLD);
    }

    //添加gridview
    private void addGridView() {
        gridView =(GridView)findViewById(R.id.gridview);
        int height = (context.getResources().getDisplayMetrics().widthPixels/7)*6;
        gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
        gridView.setOnTouchListener(new View.OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return CalendarDialog.this.gestureDetector.onTouchEvent(event);
            }
        });
    }

    /**
     * 创建菜单
     */

//    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, menu.FIRST, menu.FIRST, "今天");
        return this.onCreateOptionsMenu(menu);
    }

    /**
     * 选择菜单
     */
//    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case Menu.FIRST:
                //跳转到今天
                today();
                break;
        }
        return this.onMenuItemSelected(featureId, item);
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

    public interface OnCalendarListener {
        public void OnCalendarItemListener(Map<String,Object> map);
    }

    private class MClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_confirm:

            }
        }
    }
}
