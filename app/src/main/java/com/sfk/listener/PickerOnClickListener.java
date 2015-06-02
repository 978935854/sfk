package com.sfk.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sfk.UI.PickerView;
import com.sfk.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2015/5/29.
 */
public class PickerOnClickListener implements View.OnClickListener{
    private Context context;
    List<String> dataList;
    View pickerView;
    String selectedText=null;
    Button btn;
    public PickerOnClickListener(Context context, List<String> dataList,Button btn) {
        this.context = context;
        this.dataList = dataList;
        this.btn = btn;
    }

    public void onClick(View v) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pickerView = inflater.inflate(R.layout.picker_layout,null);
        PickerView picker_View = (PickerView) pickerView.findViewById(R.id.picker_view);
        picker_View.setData(dataList);
        pickerDialog();
        selectedText = dataList.get(dataList.size() / 2);
        picker_View.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedText=text;
            }
        });
    }
    private void pickerDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.getWindow().setContentView(pickerView);
        Button exitSelect = (Button) alertDialog.findViewById(R.id.exitSelect);
        Button centerSelect = (Button) alertDialog.findViewById(R.id.centerSelect);
        exitSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        centerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText(selectedText);
                Intent intent = new Intent();
                intent.putExtra("selectedText",selectedText);
                intent.putExtra("btn",btn.getId());
                intent.setAction("picker_seletedText");
                context.sendBroadcast(intent);
                alertDialog.dismiss();
            }
        });
    }

}
