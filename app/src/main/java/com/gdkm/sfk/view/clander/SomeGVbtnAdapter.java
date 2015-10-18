package com.gdkm.sfk.view.clander;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdkm.sfk.R;

public class SomeGVbtnAdapter extends BaseAdapter {
	private Context context;
	private int[] dataList; 
	private int resource;
	private LayoutInflater inflater;
	private Holder holder;
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private AlertDialog alertDialog;
	private WindowManager window;
	public SomeGVbtnAdapter(Context context,
			int[] dataList, int resource,AlertDialog alertDialog) {
		this.context = context;
		this.dataList = dataList;
		this.resource = resource;
		this.alertDialog = alertDialog;
		window = alertDialog.getWindow().getWindowManager();
		inflater = LayoutInflater.from(context);
		
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	
	}

	@Override
	public int getCount() {
		return dataList.length;
	}

	@Override
	public Object getItem(int arg0) {
		return dataList[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = inflater.inflate(resource, null);
			holder = new Holder();
			holder.item_text = (TextView) convertView.findViewById(R.id.item_text);
			convertView.setTag(holder);
		}else{
		    holder = (Holder) convertView.getTag();
		}
		
		holder.item_text.setText(dataList[position]+"");	
		
		//设置item宽高一样
		int itemWidth = (window.getDefaultDisplay().getWidth())/7-2;
		int itemHeight = itemWidth;
		AbsListView.LayoutParams param = new AbsListView.LayoutParams(
				itemWidth,
				itemHeight);
		convertView.setLayoutParams(param);	
		if(year_c == dataList[position] || month_c == dataList[position]){
			System.out.println("---shipeiqi--"+year_c+","+dataList[position]);
			convertView.setBackgroundResource(R.drawable.cd_calendar_item_check_bg);
			holder.item_text.setTextColor(Color.RED);
		}		
		return convertView;
	}
	public class Holder{
		TextView item_text;
		
	}

}
