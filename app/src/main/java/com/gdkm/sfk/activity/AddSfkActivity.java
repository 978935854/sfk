package com.gdkm.sfk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gdkm.sfk.R;
import com.gdkm.sfk.adapter.AddPhotoGVAdapter;
import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.pojo.Sfk;
import com.gdkm.sfk.pojo.Topic;
import com.gdkm.sfk.utils.AsyncActionInvoker;
import com.gdkm.sfk.utils.ImageZipUtil;
import com.gdkm.sfk.utils.PostMultipart;
import com.gdkm.sfk.utils.ScrollViewUtils;
import com.gdkm.sfk.utils.SetGridViewHeight;
import com.gdkm.sfk.utils.ViewUtils;
import com.gdkm.sfk.view.ImageViewDialog;
import com.gdkm.sfk.view.LineProgressBar;
import com.gdkm.sfk.view.ScollGridlView;
import com.gdkm.sfk.view.UploadProgressDialog;
import com.gdkm.sfk.view.clander.CalendarButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddSfkActivity extends Activity {
    private Context context;
    private int tid;
    public static final int ccid = 1;   //测试账号的id，到时需要替换
    private int sid;//发布表单后返回来的sid，用来上传图片录入信息
    //表单需要填写的view
    private EditText edSTitle,edScontactway,edOthermessage;

    private RadioGroup rgSpeoplenum;
    private int intSpeoplenum=1;

    private CalendarButton edLasttime;
    private RadioGroup rgStype,rgSsex;
    private Button btnAddress;
    private Spinner sage;

    private String strType = "沙发";
    private String strSex = "男女不限";
    private String strAge = "20岁以下";

    //自带物品
    private CheckBox cbToothbrush,cbTowel,cbSlipper,cbOther;
    private EditText edYourGoodsOther;
    private String strYourGoods;

    private ScrollView scrollView;
    private ScollGridlView gvAddPhoto;
    private LinearLayout llyYourgoods;
    private AddPhotoGVAdapter addPhotoGVAdapter;
    private Button btnAddsf;
    private LinearLayout llyTitle,llyContactWay,llyAddress;
    ImageViewDialog imageDialog;

    private List<Map<String,Object>> photoList = new ArrayList<Map<String, Object>>();
    private Map<String,Object> photoMap;

    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String imagePath;                //选择图片路径

    private LinearLayout llyProgressBar;
    private LineProgressBar uploadProgress;
    private PostMultipart postMultipart;
    private Map<String,Object> uploadMap;
    private UploadProgressDialog uploadProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sfk);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        tid = getIntent().getIntExtra("tid",0);
        photoMap = new HashMap<String, Object>();
        photoMap.put("imagePath", R.drawable.add_photo);
        photoList.add(photoMap);
    }

    private void initView() {

        edSTitle = (EditText) findViewById(R.id.ed_sTitle);
        edScontactway = (EditText) findViewById(R.id.ed_scontactway);
        edLasttime = (CalendarButton) findViewById(R.id.ed_lasttime);

        cbToothbrush = (CheckBox) findViewById(R.id.cb_yourgoods_toothbrush);
        cbTowel = (CheckBox) findViewById(R.id.cb_yourgoods_towel);
        cbSlipper = (CheckBox) findViewById(R.id.cb_yourgoods_slipper);
        cbOther = (CheckBox) findViewById(R.id.cb_yourgoods_other);
        cbOther.setOnCheckedChangeListener(new CbOtherCheckChangeListener());
        edYourGoodsOther = (EditText) findViewById(R.id.ed_yourgoods_other);

        edOthermessage = (EditText) findViewById(R.id.ed_othermessage);
        rgSpeoplenum = (RadioGroup) findViewById(R.id.rg_speoplenum);
        rgSpeoplenum.setOnCheckedChangeListener(new SpeoplenumCheckedChangeListener());
        rgStype = (RadioGroup) findViewById(R.id.rg_stype);
        rgStype.setOnCheckedChangeListener(new StypeCheckedChangeListener());
        rgSsex = (RadioGroup) findViewById(R.id.rg_ssex);
        rgSsex.setOnCheckedChangeListener(new SexCheckedChangeListener());
        btnAddress = (Button) findViewById(R.id.btn_address);
        sage = (Spinner) findViewById(R.id.sage);
        //填写信息输入框的父布局
        llyTitle = (LinearLayout) findViewById(R.id.lly_title);
        llyContactWay = (LinearLayout) findViewById(R.id.lly_contactWay);
        llyAddress = (LinearLayout) findViewById(R.id.lly_address);

        sage.setOnItemSelectedListener(new AgeSelectedListener());

        imageDialog = new ImageViewDialog(context);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        llyYourgoods = (LinearLayout) findViewById(R.id.lly_yourgoods);
        gvAddPhoto = (ScollGridlView) findViewById(R.id.gv_addPhoto);
        btnAddsf = (Button) findViewById(R.id.btn_addsf);
        btnAddsf.setOnClickListener(new MClick());
        addPhotoGVAdapter = new AddPhotoGVAdapter(context,photoList,R.layout.add_photo_gridview_item);
        gvAddPhoto.setAdapter(addPhotoGVAdapter);
        gvAddPhoto.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gvAddPhoto.setOnItemClickListener(new GvAddPhotoOnItemListener());
        SetGridViewHeight.setGridViewHeightBasedOnChildren(gvAddPhoto, context);
        llyProgressBar = (LinearLayout) findViewById(R.id.lly_progressBar);

        uploadProgressDialog = new UploadProgressDialog(context);
        uploadProgressDialog.setTip("正在提交信息以及图片...");
        uploadProgressDialog.setOnKeyListener("正在提交信息和图片，确定要退出？确定退出再按一次");
        uploadProgress = uploadProgressDialog.getUploadProgress();
        postMultipart = new PostMultipart(context, uploadProgress);

        if(tid ==1){
            btnAddsf.setText("申请沙发");
        }else{
            btnAddsf.setText("发布沙发");
        }
//        SetGridViewHeightTwo.setGridViewHeightBasedOnChildren(gvAddPhoto,R.layout.add_photo_gridview_item, context);
    }


    class GvAddPhotoOnItemListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String imagePath = photoList.get(position).get("imagePath")+"";
            String addPhotoDrawable = R.drawable.add_photo+"";
            if (imagePath.equals(addPhotoDrawable)){
                //选择图片
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_OPEN);
                //通过onResume()刷新数据
            }else{
                imageDialog.show();
                imageDialog.setImage(imagePath);
            }
        }
    }

    class MClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_addsf:
                    limitMessage();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 提交表单数据
     */
    private void limitMessage() {
        if (checkData()){
            uploadProgressDialog.show();
            getYourGoods();
            AsyncActionInvoker ai = new AsyncActionInvoker();
            ai.setRequestMethod("POST");
            ai.addField("sfk.stitle", edSTitle.getText() + "");
            ai.addField("sfk.scontactway",edScontactway.getText()+"");
            ai.addField("sfk.lasttime",edLasttime.getStartTime()+" 至 "+edLasttime.getEndTime());
            ai.addField("sfk.yourgoods",strYourGoods);
            ai.addField("sfk.othermessage",edOthermessage.getText()+"");
            ai.addField("sfk.speoplenum", intSpeoplenum+"");
            ai.addField("sfk.saddress",btnAddress.getText()+"");
            ai.addField("sfk.stype",strType);
            ai.addField("sfk.ssex",strSex);
            ai.addField("sfk.sage",strAge);
            ai.addField("sfk.tid",tid+"");
            ai.addField("sfk.ccid",ccid+"");
            ai.submitMessage("sfk/SfkAction!addSfk");
            ai.setOnTextClickListener(new ReturnMessageListener());
        }
    }

    /**
     * 网络请求的回调函数
     */
    class ReturnMessageListener implements AsyncActionInvoker.OnTextClickListener{
        @Override
        public void returnMessage(String text) {
            try {
                JSONObject jsonObject = new JSONObject(text);
                String ss = jsonObject.get("sfk").toString();
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                        .serializeNulls().setDateFormat("yyyy-MM-dd")
                        .setPrettyPrinting().setVersion(1.0).create();
                Sfk sfk = gson.fromJson(ss, Sfk.class);
                sid = sfk.getSid();
                uploadPhoto();//上传图片
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 校验提交的数据
     */
    private boolean checkData() {
        clearTipBg();
        if ("".equals(edSTitle.getText().toString())||null==edSTitle.getText().toString()){
            ScrollViewUtils.srcollTo(context,scrollView,llyTitle);
            Toast.makeText(context,"帖子标题不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }else if ("".equals(edScontactway.getText().toString())||null==edScontactway.getText().toString()){
            ScrollViewUtils.srcollTo(context,scrollView,llyContactWay);
            Toast.makeText(context,"联系方式不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }else if ("".equals(btnAddress.getText().toString())||null==btnAddress.getText().toString()){
            ScrollViewUtils.srcollTo(context,scrollView,llyAddress);
            Toast.makeText(context,"地点不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    /**
     * 清除提示框背景
     */
    private void clearTipBg() {
        llyTitle.setBackgroundDrawable(new BitmapDrawable());
        llyContactWay.setBackgroundDrawable(new BitmapDrawable());
        llyAddress.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 获取自带物品数据
     * @return
     */
    private void getYourGoods(){
        strYourGoods = "";
        List<String> list = new ArrayList<String>();
        if (cbToothbrush.isChecked()){
            list.add(cbToothbrush.getText()+"");
        }else if (cbTowel.isChecked()){
            list.add(cbTowel.getText()+"");
        }else if(cbSlipper.isChecked()){
            list.add(cbSlipper.getText()+"");
        }else if(cbOther.isChecked()){
            if (edYourGoodsOther.getVisibility()==View.VISIBLE){
                list.add(edYourGoodsOther.getText()+"");
            }
        }if (list.size()==0){
            strYourGoods="";
        } else {
            for (int i=0;i<list.size();i++){
                if (list.size()==1){
                    strYourGoods=list.get(0);
                } else {
                    strYourGoods = strYourGoods+","+list.get(i);
                }
                }
        }
    }

    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri, new String[] { MediaStore.Images.Media.DATA },null, null, null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                imagePath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                photoMap = new HashMap<String, Object>();
                photoMap.put("imagePath", imagePath);
                photoList.add(photoMap);
                initPhotoGridView();
            }
        }  //end if 打开图片
    }

    /**
     * 图片GridView操作
     */
    private void initPhotoGridView() {
        //整理GridView数据源
        String addPhotoDrawable = R.drawable.add_photo+"";
        for (int i=0;i<photoList.size();i++){
            String ss = photoList.get(i).get("imagePath")+"";
            if(ss.equals(addPhotoDrawable) || ss==addPhotoDrawable){
                photoList.remove(i);
            }
        }
        photoMap = new HashMap<String, Object>();
        photoMap.put("imagePath", R.drawable.add_photo);
        photoList.add(photoMap);
        //刷新GridView适配器
        addPhotoGVAdapter.notifyDataSetChanged();
        imagePath = null;
        SetGridViewHeight.setGridViewHeightBasedOnChildren(gvAddPhoto, context);
        ViewUtils.scollTo(scrollView, llyYourgoods);
//        SetGridViewHeightTwo.setGridViewHeightBasedOnChildren(gvAddPhoto,R.layout.add_photo_gridview_item, context);
    }

    /**
     * 上传图片
     */
    private void uploadPhoto() {
        final File[] files = new File[photoList.size() - 1];
        for (int i = 0; i < photoList.size() - 1; i++) {
            Map<String, Object> map = photoList.get(i);
            String tempImagePath = ImageZipUtil.saveCompressImage(map.get("imagePath") + "");
            files[i] = new File(tempImagePath);
        }
        postMultipart.addFormDataPart("photoBean.sid", sid);
        postMultipart.uploadFile(Constant.projectServicePath + "photo/PhotoAction!addSfkAction", files);
        postMultipart.setOnFileStatusListener(new PostMultipart.OnFileStatusListener() {
            @Override
            public void onFileStatusListener(boolean status) {
                if (status) {
                    uploadProgressDialog.dismiss();
                    Toast.makeText(context,"发布成功！",Toast.LENGTH_SHORT).show();
                    deleteUploadFile(files);

                    Intent intent = new Intent();
                    if (tid==1){
                        tid=2;
                    }else{
                        tid=1;
                    }
                    intent.putExtra("tid", tid);
                    intent.setAction("picker_seletedText");
                    context.sendBroadcast(intent);
                    finish();
                }
            }
        });

    }

    /**
     * 删除文件
     * @param files
     */
    private void deleteUploadFile(File[] files) {
        for (File file : files){
            if (file.exists()){
                file.delete();
            }
        }
    }

    /**
     * 沙发类型监听
     */
    class StypeCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            strType = "";
            switch (checkedId){
                case R.id.rb_shafa:
                    strType = "沙发";
                    break;
                case R.id.rb_room:
                    strType = "客厅";
                    break;
                case R.id.rb_mat:
                    strType = "地铺";
                    break;
                case R.id.rb_other:
                    strType = "其他";
                    break;
            }
        }
    }

    /**
     * 接待性别监听
     */
    class SexCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            strSex = "";
            switch (checkedId){
                case R.id.rb_boy:
                    strSex = "男";
                    break;
                case R.id.rb_girl:
                    strSex = "女";
                    break;
                case R.id.rb_boyOrGirl:
                    strSex = "男女不限";
                    break;
            }
        }
    }

    /**
     * 接待人数监听
     */
    class SpeoplenumCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            intSpeoplenum = 1;
            switch (checkedId){
                case R.id.rb_speoplenum_one:
                    intSpeoplenum = 1;
                    break;
                case R.id.rb_speoplenum_two:
                    intSpeoplenum = 2;
                    break;
                case R.id.rb_speoplenum_three:
                    intSpeoplenum = 3;
                    break;
                case R.id.rb_speoplenum_more:
                    intSpeoplenum = 0;
                    break;
            }
        }
    }

    /**
     * 自带物品其他监听
     */
    class CbOtherCheckChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                edYourGoodsOther.setVisibility(View.VISIBLE);
            }else {
                edYourGoodsOther.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 年龄限制监听
     */
    class AgeSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String strAge = context.getResources().getStringArray(R.array.age)[position];
            System.out.println("----ageSpinner---"+strAge);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
