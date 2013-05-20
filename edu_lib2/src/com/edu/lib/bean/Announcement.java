package com.edu.lib.bean;

import java.io.Serializable;

import org.json.JSONObject;

// announcements 通知
public class Announcement implements Serializable{
	private static final long serialVersionUID = 5243244012450472653L;
	public String Aorder;
	public String Sendtime;
	public String Starttime;
	public String Title;
	public String Gardenid;
	public String Sender;
	public String Classid;
	public String Issend;
	public String Contents;
	public String Endtime;
	public String Atype;
	public String Sname;
	public String Aid;
	
	public Announcement(JSONObject response){
		this.Aorder = response.optString("Aorder");
		this.Sendtime = response.optString("Sendtime");
		this.Starttime = response.optString("Starttime");
		this.Title = response.optString("Title");
		this.Gardenid = response.optString("Gardenid");
		this.Sender = response.optString("Sender");
		this.Classid = response.optString("Classid");
		this.Issend = response.optString("Issend");
		this.Contents = response.optString("Contents");
		this.Endtime = response.optString("Endtime");
		this.Atype = response.optString("Atype");
		this.Sname = response.optString("Sname");
		this.Aid = response.optString("Aid");
	}
}
