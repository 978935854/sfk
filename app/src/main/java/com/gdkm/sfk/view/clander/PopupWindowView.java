package com.gdkm.sfk.view.clander;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.gdkm.sfk.R;


public class PopupWindowView {
	private Context context;
	private PopupWindow popupwindow;
	private int[] yearDate,monthDate;
	private SomeGVbtnAdapter someGVbtnAdapter;
	private GridView some_btn_gv;
	private TextView selectText;
	private AlertDialog alertDialog;
	public PopupWindowView(Context context,AlertDialog alertDialog) {
		super();
		this.context = context;
		this.alertDialog = alertDialog;
		initData();
		initPopupWindowView();
	}



	public void initPopupWindowView() {
		if(popupwindow==null){
			// // 获取自定义布局文件pop.xml的视图
			LayoutInflater inflater = LayoutInflater.from(context);
			View customView = inflater.inflate(R.layout.cd_some_btn_gv,null);
			selectText = (TextView) customView.findViewById(R.id.selectText);
			some_btn_gv = (GridView) customView.findViewById(R.id.some_btn_gv);
			someGVbtnAdapter = new SomeGVbtnAdapter(context, yearDate, R.layout.cd_some_btn_gv_item,alertDialog);
			some_btn_gv.setAdapter(someGVbtnAdapter);
			//动态调整gridview高度

			int aa=someGVbtnAdapter.getCount()%7;
			int bb=aa>0 ?(someGVbtnAdapter.getCount()/7)+1:(someGVbtnAdapter.getCount()/7);
			int popupheight = (context.getResources().getDisplayMetrics().widthPixels/7)*bb;
			// 创建PopupWindow实例
			
			popupwindow = new PopupWindow(customView, context.getResources().getDisplayMetrics().widthPixels, LayoutParams.WRAP_CONTENT);
			popupwindow.setBackgroundDrawable(new BitmapDrawable());
			popupwindow.setFocusable(true);
			popupwindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					popupwindow.dismiss();
				}
			});
			some_btn_gv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					TextView item_text = (TextView) arg1.findViewById(R.id.item_text);
					CalendarDialog.selectDate(item_text.getText().toString());
					Toast.makeText(context, item_text.getText(), Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			popupwindow.dismiss();
			popupwindow = null;
		}
		
		
	}
	
	/*关闭PopupWindow*/
	public void dismissPW(){
		if(popupwindow.isShowing()&&popupwindow!=null){
			popupwindow.dismiss();
		}
	}
	
	/*在传参View下方显示年份PopupWindow*/
	public void showYearPW(View v){
		selectText.setText("请选择年份：");
		someGVbtnAdapter = new SomeGVbtnAdapter(context, yearDate, R.layout.cd_some_btn_gv_item,alertDialog);
		some_btn_gv.setAdapter(someGVbtnAdapter);
		popupwindow.showAsDropDown(v);
		
	}
	
	/*在传参View下方显示月份PopupWindow*/
	public void showMonthPW(View v){
		selectText.setText("请选择月份：");
		someGVbtnAdapter = new SomeGVbtnAdapter(context, monthDate, R.layout.cd_some_btn_gv_item,alertDialog);
		some_btn_gv.setAdapter(someGVbtnAdapter);
		popupwindow.showAsDropDown(v);
	}
	
	/*初始化数据*/
	public void initData(){
		yearDate = new int[42];
		int startYear = 1994;
		for(int i=0;i<42;i++){
			yearDate[i] = startYear;
			startYear++;
		}
		
		monthDate = new int[12];
		int startMonth = 1;
		for(int i=0;i<12;i++){
			monthDate[i] = startMonth;
			startMonth++;
		}
		
	}
	

}
