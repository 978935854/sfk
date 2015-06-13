package com.sfk.pojo;

import java.util.HashSet;
import java.util.Set;

/**
 * Sfk entity. @author MyEclipse Persistence Tools
 */

public class Sfk implements java.io.Serializable {

	// Fields

	private Integer sid;
	private Integer tid;
	private Integer ccid;
	private String stitle;
	private String saddress;
	private Integer stype;
	private String stime;
	private Integer speoplenum;
	private Integer ssex;
	private String sage;
	private Integer lasttime;
	private String yourgoods;
	private String scontactway;
	private String othermessage;
	private String sreleasetime;
	private Integer sdotnumber;
	private Integer sstate;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public Integer getCcid() {
		return ccid;
	}
	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}
	public String getStitle() {
		return stitle;
	}
	public void setStitle(String stitle) {
		this.stitle = stitle;
	}
	public String getSaddress() {
		return saddress;
	}
	public void setSaddress(String saddress) {
		this.saddress = saddress;
	}
	public Integer getStype() {
		return stype;
	}
	public void setStype(Integer stype) {
		this.stype = stype;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public Integer getSpeoplenum() {
		return speoplenum;
	}
	public void setSpeoplenum(Integer speoplenum) {
		this.speoplenum = speoplenum;
	}
	public Integer getSsex() {
		return ssex;
	}
	public void setSsex(Integer ssex) {
		this.ssex = ssex;
	}

    public void setSage(String sage) {
        this.sage = sage;
    }

    public String getSage() {

        return sage;
    }

    public Integer getLasttime() {
		return lasttime;
	}
	public void setLasttime(Integer lasttime) {
		this.lasttime = lasttime;
	}
	public String getYourgoods() {
		return yourgoods;
	}
	public void setYourgoods(String yourgoods) {
		this.yourgoods = yourgoods;
	}
	public String getScontactway() {
		return scontactway;
	}
	public void setScontactway(String scontactway) {
		this.scontactway = scontactway;
	}
	public String getOthermessage() {
		return othermessage;
	}
	public void setOthermessage(String othermessage) {
		this.othermessage = othermessage;
	}
	public String getSreleasetime() {
		return sreleasetime;
	}
	public void setSreleasetime(String sreleasetime) {
		this.sreleasetime = sreleasetime;
	}
	public Integer getSdotnumber() {
		return sdotnumber;
	}
	public void setSdotnumber(Integer sdotnumber) {
		this.sdotnumber = sdotnumber;
	}
	public Integer getSstate() {
		return sstate;
	}
	public void setSstate(Integer sstate) {
		this.sstate = sstate;
	}


}