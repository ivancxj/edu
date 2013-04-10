package com.edu.lib.bean;

import org.json.JSONObject;

public class Named {
	public String ID;
	public String InTime;
	public String SNum;
	public boolean IsCome;
	public boolean change;// 是否可更改
	public String Sname;
	public Named(JSONObject response) {
		this.ID = response.optString("ID");
		this.InTime = response.optString("InTime");
		this.SNum = response.optString("SNum");
		this.IsCome = response.optBoolean("IsCome");
		this.change = this.IsCome;
		this.Sname = response.optString("SName");
	}

}
