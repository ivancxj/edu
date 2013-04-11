package com.edu.lib.bean;

import org.json.JSONObject;

public class Named {
	public String InTime;
	public boolean IsRecord;
	public String SNum;
	public String SName;
	public String Memberid;
	public Named(JSONObject response) {
		this.Memberid = response.optString("Memberid");
		this.InTime = response.optString("InTime");
		this.SNum = response.optString("SNum");
		this.IsRecord = response.optBoolean("IsRecord");
		this.SName = response.optString("SName");
	}

}
