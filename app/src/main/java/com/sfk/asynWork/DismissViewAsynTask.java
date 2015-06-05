package com.sfk.asynWork;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by root on 5/17/15.
 */
public class DismissViewAsynTask extends AsyncTask {
    private TextView tv_info;

    public DismissViewAsynTask(TextView tv_info) {
        this.tv_info = tv_info;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Thread.sleep(1000 * 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        tv_info.setVisibility(View.GONE);
    }
}
