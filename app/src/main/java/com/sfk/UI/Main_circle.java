package com.sfk.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.sfk.activity.R;

/**
 * Created by Administrator on 2015/5/26.
 */
public class Main_circle extends LinearLayout {
    private Context context;
    public Main_circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.main_circle_layout, this, true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(300,0,0,paint);
//        paint.setColor(Color.GREEN);
//        canvas.drawCircle(300,500,100,paint);

    }


}
