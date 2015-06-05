package com.sfk.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sfk.activity.R;

/**
 * Created by Administrator on 2015/4/25.
 */
public class BBS_list_ImageText extends LinearLayout {
    private TextView _bbs_topicTitle=null;
    private TextView _bbs_userName=null;
    private TextView _bbs_topicTime=null;


    public BBS_list_ImageText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.bbs_list_text,this,true);
        _bbs_topicTitle = (TextView)v.findViewById(R.id.bbs_topicTitle);
        _bbs_userName = (TextView)v.findViewById(R.id.bbs_userName);
        _bbs_topicTime = (TextView)v.findViewById(R.id.bbs_topicTime);

    }



    public BBS_list_ImageText(Context context) {
        super(context);
    }

    public void set_bbs_topicTitle(String bbs_topicTitleText) {
        if(_bbs_topicTitle!=null){
            _bbs_topicTitle.setText(bbs_topicTitleText);
        }
    }

    public void set_bbs_userName(String bbs_userNameText) {
        if(_bbs_userName!=null){
            _bbs_userName.setText(bbs_userNameText);
        }
    }

    public void set_bbs_topicTime(String bbs_topicTimeText) {
        if(_bbs_topicTime!=null){
            _bbs_topicTime.setText(bbs_topicTimeText);
        }
    }








}
