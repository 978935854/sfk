package com.gdkm.sfk.pojo;

import java.util.List;

/**
 * Created by zxw on 2015/6/1.
 */
public class Topic {
    private Integer topicid;
    private Integer cid;
    private String topictitle;
    private String topiccontent;
    private Integer tgood;
    private Integer tbad;
    private Integer tdotnumber;
    private String treleasetime;
    private Integer tstate;

    /*新增的bean字段，数据库没有*/
    private String ccusername;
    private List<String> photoList;
    private String cemail;
    private String ctelnum;
    public String getCemail() {
        return cemail;
    }
    public void setCemail(String cemail) {
        this.cemail = cemail;
    }
    public String getCtelnum() {
        return ctelnum;
    }
    public void setCtelnum(String ctelnum) {
        this.ctelnum = ctelnum;
    }

    public List<String> getPhotoList() {
        return photoList;
    }
    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }
    public String getCcusername() {
        return ccusername;
    }
    public void setCcusername(String ccusername) {
        this.ccusername = ccusername;
    }

    public Integer getTopicid() {
        return topicid;
    }

    public void setTopicid(Integer topicid) {
        this.topicid = topicid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getTopictitle() {
        return topictitle;
    }

    public void setTopictitle(String topictitle) {
        this.topictitle = topictitle;
    }

    public String getTopiccontent() {
        return topiccontent;
    }

    public void setTopiccontent(String topiccontent) {
        this.topiccontent = topiccontent;
    }

    public Integer getTgood() {
        return tgood;
    }

    public void setTgood(Integer tgood) {
        this.tgood = tgood;
    }

    public Integer getTbad() {
        return tbad;
    }

    public void setTbad(Integer tbad) {
        this.tbad = tbad;
    }

    public Integer getTdotnumber() {
        return tdotnumber;
    }

    public void setTdotnumber(Integer tdotnumber) {
        this.tdotnumber = tdotnumber;
    }

    public String getTreleasetime() {
        return treleasetime;
    }

    public void setTreleasetime(String treleasetime) {
        this.treleasetime = treleasetime;
    }

    public Integer getTstate() {
        return tstate;
    }

    public void setTstate(Integer tstate) {
        this.tstate = tstate;
    }
}