package com.sfk.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.sfk.activity.R;

/**
 * Created by Administrator on 2015/6/5.
 */
public class Scenic_AddressUtil {
    private Context context;
    private Spinner spprovinces,sscenic;
    public String scenic,provinces=null;
    public Scenic_AddressUtil(Context context) {
        this.context = context;
    }

    public void scenic_address(Spinner spprovinces, Spinner sscenic){
        this.spprovinces=spprovinces;
        this.sscenic=sscenic;
        //处理省份下拉框的显示
        setProvinces();
        //处理城市下拉框的显示
        setcities();
    }

    //处理省份下拉框的显示
    private void setProvinces(){
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.provinces, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spprovinces.setAdapter(adapter);
        spprovinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Spinner spinner = (Spinner) parent;
                String pro = (String) spinner.getItemAtPosition(position);

                //处理省对应市的显示
                ArrayAdapter<CharSequence> scenicAdapter = ArrayAdapter.createFromResource(context, R.array.provinces, android.R.layout.simple_spinner_dropdown_item);
                if (pro.equals("北京市")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_beijing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("天津市")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_tianjing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("河北省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_hebei, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("山西省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_shanxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("内蒙古自治区")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_neimenggu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("辽宁省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_liaoling, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("吉林省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_jilin, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("黑龙江省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_heilongjiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("上海市")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_shanghai, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("江苏省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_jiangsu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("浙江省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_zhejiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("安徽省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_anhui, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("福建省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_fujian, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("江西省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_jiangxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("山东省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_shandong, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("河南省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_henan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("湖北省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_hubei, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("湖南省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_hunan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("广东省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_guangdong, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("广西壮族自治区")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_guangxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("海南省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_hainan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("重庆市")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_chongxing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("四川省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_sichuan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("贵州省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_beijing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("云南省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_yunnan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("西藏自治区")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_xizang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("陕西省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_shanxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("甘肃省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_gansu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("青海省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_qinghai, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("宁夏回族自治区")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_ningxia, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("新疆维吾尔自治区")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_xingjiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("台湾省")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_taiwan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("香港特别行政区")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_xianggang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("澳门特别行政区")) {
                    scenicAdapter = ArrayAdapter.createFromResource(context, R.array.scenic_aomeng, android.R.layout.simple_spinner_dropdown_item);
                }

                sscenic.setAdapter(scenicAdapter);
                provinces=spprovinces.getSelectedItem().toString();
                Toast.makeText(context, provinces, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });


    }
    //监听市下拉框，获取选取内容
    private String setcities() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.provinces, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sscenic.setAdapter(adapter);
        sscenic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {
                //获取选择的市的值
                scenic=adapter.getItemAtPosition(position).toString();
//                Toast.makeText(context, cities, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return scenic;
    }

}
