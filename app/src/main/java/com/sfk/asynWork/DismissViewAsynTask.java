package com.sfk.asynWork;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by root on 5/17/15.
 */
public class DismissViewAsynTask extends AsyncTask<String, String, String> {
    private TextView tv_info;

    public DismissViewAsynTask(TextView tv_info) {
        this.tv_info = tv_info;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Thread.sleep(1000 * 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        tv_info.setVisibility(View.GONE);
    }
}
