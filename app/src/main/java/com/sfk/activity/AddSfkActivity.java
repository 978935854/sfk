package com.sfk.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.*;
import com.sfk.utils.AsyncHttpClientUtil;

import org.apache.http.Header;
import org.json.JSONObject;


public class AddSfkActivity extends ActionBarActivity {
    private EditText ettitle,etinfo,etscontactway,etaccount,ettype,etlasttime,etyourgoods,etothermessage,etspeoplenum;
    private Spinner spstypesp,spprovinces,spsage,spcities,spsex;
    private ImageButton addImage,addSfk;
    private ImageView imageView;
    private RequestParams params;
    private String title,tinfo,scontactway,account,stype,lasttime,yourgoods,othermessage,speoplenum,ssex,provinces,sage,cities,state,stime,sreleasetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sfk);
        setView();
        setProvinces();
        setcities();
        getAddSfkInfo();
        addSfkOnclick();


    }


    //处理省份下拉框的显示
    private void setProvinces(){
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.provinces, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spprovinces.setAdapter(adapter);
        spprovinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Spinner spinner = (Spinner) parent;
                String pro = (String) spinner.getItemAtPosition(position);

                //处理省对应市的显示
                ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.provinces, android.R.layout.simple_spinner_dropdown_item);
                if (pro.equals("北京市")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.beijing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("天津市")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.tianjing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("河北省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.hebei, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("山西省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.shanxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("内蒙古自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.neimenggu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("辽宁省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.liaoling, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("吉林省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.jilin, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("黑龙江省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.heilongjiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("上海市")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.shanghai, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("江苏省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.jiangsu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("浙江省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.zhejiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("安徽省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.anhui, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("福建省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.fujian, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("江西省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.jiangxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("山东省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.shandong, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("河南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.henan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("湖北省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.hubei, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("湖南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.hunan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("广东省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.guangdong, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("广西壮族自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.guangxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("海南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.hainan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("重庆市")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.chongxing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("四川省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.sichuan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("贵州省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.beijing, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("云南省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.yunnan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("西藏自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.xizang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("陕西省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.shanxi, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("甘肃省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.gansu, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("青海省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.qinghai, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("宁夏回族自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.ningxia, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("新疆维吾尔自治区")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.xingjiang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("台湾省")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.taiwan, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("香港特别行政区")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.xianggang, android.R.layout.simple_spinner_dropdown_item);
                } else if (pro.equals("澳门特别行政区")) {
                    cityAdapter = ArrayAdapter.createFromResource(AddSfkActivity.this, R.array.aomeng, android.R.layout.simple_spinner_dropdown_item);
                }

                spcities.setAdapter(cityAdapter);
                provinces=spprovinces.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), provinces, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });


    }
    //监听市下拉框，获取选取内容
    private void setcities() {
        spcities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {
                //获取选择的市的值
                String cities=adapter.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), cities, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


   //发布沙发按钮监听，向服务器发送发布的沙发信息
    private void addSfkOnclick(){
        addSfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://192.168.1.160:8080/shaFaKe/sfk/SfkAction!addSfk";
                AsyncHttpClientUtil.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.i("errorrrrrrrrrrrrr","数据返回出错！");
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONObject jsonObject=response.getJSONObject("sfk");
                            Log.i("ttttttttttttttttttttt",jsonObject.toString());
                            //Log.i("ttttttttttttttttttttt",jsonObject.getString("saddress"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void setView() {
        ettitle=(EditText)findViewById(R.id.title);
        etinfo=(EditText)findViewById(R.id.info);
        etscontactway=(EditText)findViewById(R.id.scontactway);
        etaccount=(EditText)findViewById(R.id.account);
        ettype=(EditText)findViewById(R.id.type);
        etlasttime=(EditText)findViewById(R.id.lasttime);
        etyourgoods=(EditText)findViewById(R.id.yourgoods);
        etothermessage=(EditText)findViewById(R.id.othermessage);
        etyourgoods.setText("客户端向服务器端发送信息！！！");
        etspeoplenum=(EditText)findViewById(R.id.speoplenum);
        spstypesp=(Spinner)findViewById(R.id.stypesp);
        spprovinces=(Spinner)findViewById(R.id.provinces);
        spcities=(Spinner)findViewById(R.id.cities);
        spsage=(Spinner)findViewById(R.id.sage);
        spsex=(Spinner)findViewById(R.id.ssex);
        addImage=(ImageButton)findViewById(R.id.upImage);
        addSfk=(ImageButton)findViewById(R.id.addsfk);
        imageView=(ImageView)findViewById(R.id.imageView);

        //获取填入要发布的沙发信息
        title=ettitle.getText().toString();
        tinfo=etinfo.getText().toString();
        scontactway=etscontactway.getText().toString();
        account=etaccount.getText().toString();
        stype=ettype.getText().toString();
        lasttime=etlasttime.getText().toString();
        yourgoods=etyourgoods.getText().toString();
        othermessage=etothermessage.getText().toString();
        speoplenum=etspeoplenum.getText().toString();

        sage=spsage.getSelectedItem().toString();

        ssex=spsex.getSelectedItem().toString();
    }

    //获取要向服务器发送的数据
    public void getAddSfkInfo() {
        params = new RequestParams();
        params.put("title",title);
        params.put("tinfo",tinfo);
        params.put("scontactway",scontactway);
        params.put("account",account);
        params.put("type",stype);
        params.put("lasttime",lasttime);
        params.put("yourgoods",yourgoods);
        params.put("othermessage",othermessage);
        params.put("speoplenum",speoplenum);
 //       params.put("provinces",provinces);
        params.put("sage",sage);
 //       params.put("cities",cities);
        params.put("stype",stype);
        params.put("stime",stime);
        params.put("sreleasetime",sreleasetime);
    }
}
