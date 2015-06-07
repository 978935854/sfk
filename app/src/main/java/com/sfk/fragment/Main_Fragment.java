package com.sfk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sfk.activity.AddSfkActivity;
import com.sfk.activity.R;
import com.sfk.activity.SeekSFActivity;

/**
 * Created by Administrator on 2015/5/16.
 */
public class Main_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);
        //寻找沙发
        ImageButton seekSF_btn = (ImageButton) view.findViewById(R.id.seekSF_btn);
        seekSF_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeekSFActivity.class);
                startActivity(intent);
            }
        });
        //发布沙发
        ImageButton releaseSF_btn = (ImageButton) view.findViewById(R.id.seek_sf_topic_listView);
        releaseSF_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSfkActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
