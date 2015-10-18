package com.gdkm.sfk.application;

import android.app.Application;

/**
 * Created by Kiki on 2015/5/28.
 */
public class LoginApplication extends Application implements java.io.Serializable {
    private int cid;
    private String ctelnum;
    private String cemail;
    private String nickName;
    private String openId;
    private String headPath;

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }
    public String getHeadPath() {
        return headPath;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {

        return nickName;
    }

    public String getOpenId() {
        return openId;
    }

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

}
