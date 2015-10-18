package com.gdkm.sfk.pojo;

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
	private String stype;
	private String stime;
	private Integer speoplenum;
	private String ssex;
	private String sage;
	private String lasttime;
	private String yourgoods;
	private String scontactway;
	private String othermessage;
	private String sreleasetime;
	private Integer sdotnumber;
	private Integer sstate;
	private Integer startPosition;

	public void setStartPosition(Integer startPosition) {
		this.startPosition = startPosition;
	}

	public void setEndPosition(Integer endPosition) {
		this.endPosition = endPosition;
	}

	private Integer endPosition;

	public Integer getStartPosition() {
		return startPosition;
	}

	public Integer getEndPosition() {
		return endPosition;
	}

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

	public void setStype(String stype) {
		this.stype = stype;
	}

	public void setSsex(String ssex) {
		this.ssex = ssex;
	}

	public String getStype() {
		return stype;
	}

	public String getSsex() {
		return ssex;
	}

	public void setSage(String sage) {
        this.sage = sage;
    }

    public String getSage() {

        return sage;
    }

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public String getLasttime() {

		return lasttime;
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