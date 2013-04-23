package com.edu.lib.bean;

import org.json.JSONObject;

public class Named {
	public String InTime;// 考勤时间
	public boolean IsRecord;// 刷卡状态
	public String SNum;// 学生编号 
	public String SName; // 学生名称
	public String Memberid; // 用户memberid
	public String Remark; // Remark
	public Named(JSONObject response) {
		this.Memberid = response.optString("Memberid");
		this.InTime = response.optString("InTime");
		this.SNum = response.optString("SNum");
		this.IsRecord = response.optBoolean("IsRecord");
		this.SName = response.optString("SName");
		this.Remark = response.optString("Remark");
	}

}
