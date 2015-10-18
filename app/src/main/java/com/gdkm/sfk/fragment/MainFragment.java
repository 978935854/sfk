package com.gdkm.sfk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.gdkm.sfk.R;
import com.gdkm.sfk.activity.SeekSFActivity;

/**
 * Created by Administrator on 2015/5/16.
 */
public class MainFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment,container,false);
        initview();

        return view;
    }

    private void initview() {
        //申请沙发
        Button seekSF_btn = (Button) view.findViewById(R.id.seekSF_btn);
        seekSF_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeekSFActivity.class);
                intent.putExtra("tid",1);
                startActivity(intent);
            }
        });
        //我有沙发
        Button releaseSF_btn = (Button) view.findViewById(R.id.seek_sf_topic_listView);
        releaseSF_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeekSFActivity.class);
                intent.putExtra("tid",2);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
