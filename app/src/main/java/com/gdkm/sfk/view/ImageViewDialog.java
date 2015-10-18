package com.gdkm.sfk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gdkm.sfk.R;

/**
 * Created by Administrator on 2015/9/17.
 * 使用方法，
 * 1、new ImageViewDialog();
 * 2、show();
 * 3、setImage(String imagePath)
 */
public class ImageViewDialog extends AlertDialog {
    private Context context;
    private DragImageView customImageView;
    private View view;
    public ImageViewDialog(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.dlg_photo,null);
        customImageView = (DragImageView) view.findViewById(R.id.sfinfophoto);
        Button btnClose = (Button) view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new MClick());

    }

    public ImageViewDialog(Context context, int theme) {
        super(context, theme);
    }

    public ImageViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
    }

    public void setImage(String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        customImageView.setImageBitmap(bitmap);
        customImageView.setOnTouchAction(new DragImageView.OnTouchAction() {
            @Override
            public void onTouchDownAndUp(boolean status) {
                System.out.println("---onTouchDownAndUp---");
                if (status) {
                    dismiss();
                }
            }
        });
    }

    class MClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (this!=null && isShowing()){
                dismiss();
            }
        }
    }
}
