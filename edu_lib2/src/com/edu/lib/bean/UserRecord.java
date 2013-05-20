package com.edu.lib.bean;

import org.json.JSONObject;

public class UserRecord {
	public String systime;//出勤时间（若没出勤，则为系统时间）
	public String SName;//学生名称
	public String SNum;//学生4位学号
	public String InTime;//刷卡时间
	public String Memberid;//学生的guid
	public int isRecord;//是否已刷卡
	public String Remark;//备注

	// CardRecord
	public UserRecord(JSONObject response){
		this.systime = response.optString("systime");
		if(!response.isNull("SName"))
			this.SName = response.optString("SName");
		if(!response.isNull("SNum"))
			this.SNum = response.optString("SNum");
		if(!response.isNull("InTime"))
		this.InTime = response.optString("InTime");
		if(!response.isNull("Memberid"))
		this.Memberid = response.optString("Memberid");
		if(!response.isNull("isRecord"))
			this.isRecord = response.optInt("isRecord");
		if(!response.isNull("Remark"))
			this.Remark = response.optString("Remark");
	}
}
