package com.sfk.pojo;

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
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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