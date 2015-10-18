package com.gdkm.sfk.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gdkm.sfk.R;


/**
 * Created by Administrator on 2015/6/5.
 */
public class AddressUtil {
    private Context context;
    private Spinner spprovinces,spcities;
    public String cities=null,provinces=null;
    public AddressUtil(Context context) {
        this.context = context;
    }

    public void address(Spinner spprovinces, Spinner spcities){
        this.spprovinces=spprovinces;
        this.spcities=spcities;
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
                ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(context, R.array.provinces, android.R.layout.simple_spinner_dropdown_item);
                if (pro.equals("北京市")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.beijing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("天津市")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.tianjing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("河北省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.hebei, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("山西省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.shanxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("内蒙古自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.neimenggu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("辽宁省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.liaoling, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("吉林省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.jilin, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("黑龙江省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.heilongjiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("上海市")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.shanghai, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("江苏省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.jiangsu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("浙江省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.zhejiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("安徽省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.anhui, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("福建省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.fujian, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("江西省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.jiangxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("山东省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.shandong, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("河南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.henan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("湖北省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.hubei, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("湖南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.hunan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("广东省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.guangdong, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("广西壮族自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.guangxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("海南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.hainan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("重庆市")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.chongxing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("四川省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.sichuan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("贵州省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.beijing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("云南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.yunnan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("西藏自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.xizang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("陕西省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.shanxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("甘肃省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.gansu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("青海省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.qinghai, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("宁夏回族自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.ningxia, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("新疆维吾尔自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.xingjiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("台湾省")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.taiwan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("香港特别行政区")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.xianggang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("澳门特别行政区")) {
                    cityAdapter = ArrayAdapter.createFromResource(context, R.array.aomeng, android.R.layout.simple_spinner_dropdown_item);
                }

                spcities.setAdapter(cityAdapter);
                provinces=spprovinces.getSelectedItem().toString();
                //Toast.makeText(context, provinces, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });


    }
    //监听市下拉框，获取选取内容
    public String setcities() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.provinces, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcities.setAdapter(adapter);
        spcities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {
                ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(context, R.array.beijing, android.R.layout.simple_spinner_dropdown_item);
                //获取选择的市的值
                cities = adapter.getItemAtPosition(position).toString();
                //Toast.makeText(context, cities, Toast.LENGTH_LONG).show();
                Spinner spinner = (Spinner) adapter;
                String pro = (String) spinner.getItemAtPosition(position);
                //Toast.makeText(context, pro, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return cities;
    }

}
