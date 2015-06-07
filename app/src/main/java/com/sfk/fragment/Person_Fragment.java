package com.sfk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sfk.activity.R;
import com.sfk.activity.Register;

/**
 * Created by Administrator on 2015/5/16.
 */
public class Person_Fragment extends Fragment {
    private Button btn_login_or_register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_fragment, container, false);
        btn_login_or_register = (Button) view.findViewById(R.id.btn_login_or_register);
        btn_login_or_register.setOnClickListener(new Listener(this.getActivity()));

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class Listener implements View.OnClickListener {
        private Context context;
        public Listener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_login_or_register) {
                Intent intent = new Intent(context, Register.class);
                context.startActivity(intent);
            }
        }
    }
}
