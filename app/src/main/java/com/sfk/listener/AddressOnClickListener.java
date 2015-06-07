package com.sfk.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sfk.activity.R;
import com.sfk.utils.AddressUtil;
import com.sfk.utils.Scenic_AddressUtil;


public class AddressOnClickListener implements View.OnClickListener {
    private Context context;
    AddressUtil addressUtil;
    Scenic_AddressUtil scenic_addressUtil;
    Button btn;
    View view;
    AlertDialog builder;
    Spinner seeksf_provinces,seeksf_selector_2;
    TextView seek_tips_2;
    public AddressOnClickListener(Context context,Button btn) {
        this.context = context;
        this.btn=btn;
    }

    @Override
    public void onClick(View v) {
        //实例化dialog窗口
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.address_selector,null);
        builder = new AlertDialog.Builder(context).create();
        builder.show();
        builder.getWindow().setContentView(view);
        seeksf_provinces = (Spinner)view.findViewById(R.id.seeksf_provinces);
        seeksf_selector_2 = (Spinner)view.findViewById(R.id.seeksf_selector_2);
        seek_tips_2 = (TextView) view.findViewById(R.id.seek_tips_2);

        all_addressDialog();//首次加载全部的选项

        final RelativeLayout scenic_cities_layout = (RelativeLayout) view.findViewById(R.id.scenic_cities_layout);
        RadioGroup select_scenic_cities = (RadioGroup) view.findViewById(R.id.select_scenic_cities);
        select_scenic_cities.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.scenic_cities_radioButton:    //全部查询
                        scenic_cities_layout.setVisibility(View.INVISIBLE);
                        all_addressDialog();
                        break;
                    case R.id.scenic_radioButton:   //根据热门景点查询
                        scenic_cities_layout.setVisibility(View.VISIBLE);
                        scenic_addressDialog();
                        break;
                    case R.id.cities_radioButton:   //根据省市查询
                        scenic_cities_layout.setVisibility(View.VISIBLE);
                        addressDialog();
                        break;
                }
            }
        });


    }
    //全部的选项
    private void all_addressDialog(){
        Button address_exitSelect = (Button) builder.findViewById(R.id.address_exitSelect);
        address_exitSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        Button address_centerSelect = (Button) builder.findViewById(R.id.address_centerSelect);
        address_centerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText("地点(全部)");
                Intent intent = new Intent();
                intent.setAction("picker_seletedText");
                context.sendBroadcast(intent);  //发送广播，更新数据
                builder.dismiss();
            }
        });
    }

    //根据热门景点查询
    private void scenic_addressDialog(){
        seek_tips_2.setText("景点：");
        scenic_addressUtil = new Scenic_AddressUtil(context);         //省市选择器
        scenic_addressUtil.scenic_address(seeksf_provinces, seeksf_selector_2);
        Button address_exitSelect = (Button) builder.findViewById(R.id.address_exitSelect);
        address_exitSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        Button address_centerSelect = (Button) builder.findViewById(R.id.address_centerSelect);
        address_centerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断省景点直接的关系，景点为空，直接取省名
                if ("".equals(scenic_addressUtil.scenic)||scenic_addressUtil.scenic==null
                        ){
                    Log.i("scenic_addressUtil",scenic_addressUtil.provinces);
                    btn.setText(scenic_addressUtil.provinces);
                }else {
                    btn.setText(scenic_addressUtil.scenic);
                }
                Intent intent = new Intent();
                intent.setAction("picker_seletedText");
                context.sendBroadcast(intent);  //发送广播，更新数据
                builder.dismiss();

            }
        });
    }

    //根据省市查询
    private void addressDialog(){
        seek_tips_2.setText("城市：");
        addressUtil = new AddressUtil(context);         //省市选择器
        addressUtil.address(seeksf_provinces,seeksf_selector_2);
        Button address_exitSelect = (Button) builder.findViewById(R.id.address_exitSelect);
        address_exitSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        Button address_centerSelect = (Button) builder.findViewById(R.id.address_centerSelect);
        address_centerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断省市直接的关系，城市是直辖之类，直接取省名
                if ("市辖区".equals(addressUtil.cities)
                        || "县".equals(addressUtil.cities)
                        || "市".equals(addressUtil.cities)
                        || "省直辖行政单位".equals(addressUtil.cities)
                        || "省直辖县级行政单位".equals(addressUtil.cities)
                        ){
                    btn.setText(addressUtil.provinces);
                }else {
                    btn.setText(addressUtil.cities);
                }
                Intent intent = new Intent();
                intent.setAction("picker_seletedText");
                context.sendBroadcast(intent);  //发送广播，更新数据
                builder.dismiss();

            }
        });
    }
}
