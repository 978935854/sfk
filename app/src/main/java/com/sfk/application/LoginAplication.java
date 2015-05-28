package com.sfk.application;

import android.app.Application;

/**
 * Created by Kiki on 2015/5/28.
 */
public class LoginAplication extends Application implements java.io.Serializable {
    private int cid;
    private String ctelnum;
    private String cemail;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCtelnum() {
        return ctelnum;
    }

    public void setCtelnum(String ctelnum) {
        this.ctelnum = ctelnum;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public void onCreate() {
        super.onCreate();


    }
}
