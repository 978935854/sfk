package com.gdkm.sfk.view.clander;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdkm.sfk.R;
import com.gdkm.sfk.utils.BaseAddress;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * Created by Administrator on 2015/9/22.
 */
public class CalendarButton extends Button{
    private Context context;
    private CalendarDialog calendarDialog;
    private String startTime;
    private String endTime;


    public CalendarButton(Context context) {
        super(context);
        this.context = context;
        calendarDialog = new CalendarDialog(context);
        initButtonView();
    }

    public CalendarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        calendarDialog = new CalendarDialog(context);
        initButtonView();
    }

    public CalendarButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        calendarDialog = new CalendarDialog(context,R.style.cornerDialog);
        initButtonView();
    }

    private void initButtonView() {
        this.setOnClickListener(new MClick());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        setText(formatter.format(new Date()));
        setBackgroundResource(R.drawable.cd_calendar_btn_shape);
        setTextColor(context.getResources().getColor(R.color.dark_gray));
        setPadding(10, 5, 10, 5);
        Drawable time_icon = context.getResources().getDrawable(R.drawable.time_icon);
        time_icon.setBounds(5, 0, time_icon.getMinimumWidth(), time_icon.getMinimumHeight());
        setCompoundDrawables(time_icon, null, null, null);
        setCompoundDrawablePadding(20);
        setMinimumWidth(dip2px(context, 100f));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date= new Date();
        String startTime = sdf.format(date);
        date.setDate(date.getDate() + 1);
        String endTime = sdf.format(date);
        setText("开始入住：" + startTime + "\n离开时间：" + endTime + "\n" );
        setStartTime(startTime);
        setEndTime(endTime);
    }



    class MClick implements OnClickListener{
        @Override
        public void onClick(View v) {
            calendarDialog.show();
            calendarDialog.setOnCalendarListener(new CalendarDialog.OnCalendarListener() {
                @Override
                public void OnCalendarItemListener(Map<String,Object> map) {
                    String startTime = map.get("startTime")+"";
                    String endTime = map.get("endTime")+"";
                    String checkNumber = map.get("checkNumber")+"";
                    setText("开始入住："+startTime+"\n离开时间："+endTime+"\n"+checkNumber+"天");
                    setStartTime(startTime);
                    setEndTime(endTime);
//                    setText(text);
                }
            });
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 开始入住时间
     * @return
     */
    public String getStartTime() {
        return startTime;

    }

    /**
     * 离开时间
     * @return
     */
    public String getEndTime() {
        return endTime;
    }
}
