package com.gdkm.sfk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gdkm.sfk.R;
import com.gdkm.sfk.utils.BaseAddress;
import com.gdkm.sfk.wheel.adapters.ArrayWheelAdapter;
import com.gdkm.sfk.wheel.widget.OnWheelChangedListener;
import com.gdkm.sfk.wheel.widget.WheelView;

import java.util.HashMap;
import java.util.Map;
//import com.gdkm.sfk.wheel.adapters
/**
 * 地点选择器
 * Created by Administrator on 2015/9/6.
 */
public class AddressButton extends Button implements View.OnClickListener, OnWheelChangedListener {
    private static final String TAG = "AddressButton";
    private Context context;
    private View view;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm,btnCancel;

    private AlertDialog dialog;
    private BaseAddress baseAddress;
    private Map<String,Object> addressMap;
    private String provinceName = "";
    private String cityName = "";
    private String districtName = "";
    private boolean checked ;
    public AddressButton(Context context) {
        super(context);
        this.context = context;
    }

    public AddressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        addressMap = new HashMap<String, Object>();
        initView();
    }

    private void initView() {
        this.setOnClickListener(new MClick());
    }

    class MClick implements OnClickListener{
        @Override
        public void onClick(View v) {
            initDialog();
        }
    }

    /**
     * 初始化弹出框dialog
     */
    private void initDialog() {
        view = LayoutInflater.from(context).inflate(R.layout.dlg_address,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        baseAddress = new BaseAddress(context);
        checked = true;
        setUpViews();
        setUpListener();
        setUpData();

    }

    /**
     * 设置view
     */
    private void setUpViews() {
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        mViewCity = (WheelView) view.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
        mBtnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
    }

    /**
     * 添加点击事件
     */
    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
        // 添加onclick事件
        btnCancel.setOnClickListener(this);
    }

    /**
     * 设置地区数据
     */
    private void setUpData() {
        baseAddress.initProvinceDatas();
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        initProvince();
        updateCities();
        updateAreas();
    }

    /**
     * 省份滑动改变
     */
    private void initProvince() {
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, baseAddress.mProvinceDatas));
        if (null!=addressMap  && checked){
            provinceName = addressMap.get("provinceName")+"";
            cityName = addressMap.get("cityName")+"";
            districtName = addressMap.get("districtName")+"";
            for (int i=0;i<baseAddress.mProvinceDatas.length;i++){
                if (provinceName.equals(baseAddress.mProvinceDatas[i])){
                    mViewProvince.setCurrentItem(i);
                    System.out.println("-----provinceName----"+baseAddress.mProvinceDatas[i]);
                }
            }
        }
    }

    /**
     * 地区滑动改变
     * @param wheel the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            mViewCity.setCurrentItem(0);
            updateCities();
        } else if (wheel == mViewCity) {
            mViewDistrict.setCurrentItem(0);
            updateAreas();
        } else if (wheel == mViewDistrict) {
            baseAddress.mCurrentDistrictName = baseAddress.mDistrictDatasMap.get(baseAddress.mCurrentCityName)[newValue];
            baseAddress.mCurrentZipCode = baseAddress.mZipcodeDatasMap.get(baseAddress.mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        System.out.println("pCurrent:"+pCurrent);
        baseAddress.mCurrentCityName = baseAddress.mCitisDatasMap.get(baseAddress.mCurrentProviceName)[pCurrent];
        String[] areas = baseAddress.mDistrictDatasMap.get(baseAddress.mCurrentCityName);
        baseAddress.mCurrentDistrictName = areas[0];
        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        if(null != baseAddress.mDistrictDatasMap.get(cityName) && checked){
            for (int i=0;i<baseAddress.mDistrictDatasMap.get(cityName).length;i++){
                if (districtName.equals(baseAddress.mDistrictDatasMap.get(cityName)[i])){
                    mViewDistrict.setCurrentItem(i);
                    checked = false;
                    System.out.println("-----districtDatas----"+baseAddress.mDistrictDatasMap.get(cityName)[i]+",i="+i);
                }
            }
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        baseAddress.mCurrentProviceName = baseAddress.mProvinceDatas[pCurrent];
        String[] cities = baseAddress.mCitisDatasMap.get(baseAddress.mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
//        mViewCity.setCurrentItem(0);
        if(null != baseAddress.mCitisDatasMap.get(provinceName)  && checked){
            for (int i=0;i<baseAddress.mCitisDatasMap.get(provinceName).length;i++){
                if (cityName.equals(baseAddress.mCitisDatasMap.get(provinceName)[i])){
                    mViewCity.setCurrentItem(i);
                    System.out.println("-----cityName----"+baseAddress.mCitisDatasMap.get(provinceName)[i]+",i="+i);
                }
            }
        }
        updateAreas();
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                showSelectedResult();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 点击确定后返回来数据函数
     */
    private void showSelectedResult() {
        dialog.dismiss();
        this.setText(baseAddress.mCurrentProviceName + baseAddress.mCurrentCityName +
                baseAddress.mCurrentDistrictName);
        addressMap.put("provinceName", baseAddress.mCurrentProviceName);
        addressMap.put("cityName",baseAddress.mCurrentCityName);
        addressMap.put("districtName",baseAddress.mCurrentDistrictName);
//        Toast.makeText(context, "当前选中:" + baseAddress.mCurrentProviceName + "," + baseAddress.mCurrentCityName + ","
//                + baseAddress.mCurrentDistrictName + "," + baseAddress.mCurrentZipCode, Toast.LENGTH_SHORT).show();
    }

}
