package com.gdkm.sfk.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdkm.sfk.R;
import com.gdkm.sfk.utils.DipAndPxUtil;

/**
 * Created by Administrator on 2015/9/17.
 * 使用方法，
 * 1、new ImageViewDialog();
 * 2、show();
 * 3、setImage(String imagePath)
 */
public class UploadProgressDialog {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private int backDotNumber=0;
    private long startTime=0,lastTime=0;
    private TextView tvLoad;
    private LineProgressBar uploadProgress;
    public UploadProgressDialog(Context context) {
        this.context = context;
        initDialog();
    }

    public LineProgressBar getUploadProgress() {
        return uploadProgress;
    }

    private void initDialog() {
        dialogView = LayoutInflater.from(context).inflate(R.layout.center_uploadprogress_dlg,null);
        tvLoad = (TextView) dialogView.findViewById(R.id.tv_load);
        tvLoad.setText("正在提交信息以及图片...");

        uploadProgress = (LineProgressBar) dialogView.findViewById(R.id.upload_progress);
        uploadProgress.setmTextColor(context.getResources().getColor(R.color.text_color));//设置显示百分比数字颜色
        uploadProgress.setmReachedBarColor(context.getResources().getColor(R.color.theme_blue));//设置已加载线条颜色
        uploadProgress.setmUnReachedBarColor(context.getResources().getColor(R.color.theme_deep_blue));//设置未加载线条颜色
        uploadProgress.setmReachedProgressBarHeight(5);//设置已加载线条高度
        uploadProgress.setmUnReachedProgressBarHeight(5);//设置未加载线条高度

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.dismiss();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DipAndPxUtil.dip2px(context, 200);
        params.height = DipAndPxUtil.dip2px(context,200);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setContentView(dialogView);

    }

    public void dismiss(){
        if (alertDialog.isShowing() && alertDialog!=null){
            alertDialog.dismiss();
        }
    }

    public void show(){
        if (alertDialog!=null){
            alertDialog.show();
        }
    }

    public void setTip(String tip){
        tvLoad.setText(tip);
    }

    public void setOnKeyListener(String message){
        alertDialog.setOnKeyListener(new MyOnKeyListener(message));
    }

    class MyOnKeyListener implements DialogInterface.OnKeyListener{
        String message;

        public MyOnKeyListener(String message) {
            this.message = message;
        }

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (backDotNumber==0){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    startTime = System.currentTimeMillis();
                    backDotNumber++;
                }else{
                    lastTime = System.currentTimeMillis();
                    if (lastTime-startTime>=1500){
                        startTime=0;
                        lastTime=0;
                        backDotNumber=0;
                    }else{
                        ((Activity)context).finish();
                    }
                }
            }
            return false;
        }
    }
}
