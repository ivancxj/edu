package com.edu.lib.bean;

import org.json.JSONObject;

public class TeacherInfo {
	
	public String TMobile;
	public String TPos;// 职务
	public String TName;
	public String Userid;// 老师的 id
	public String ClassName;// 班级
	// mobileitemteachers
	public TeacherInfo(JSONObject response){
		this.TMobile = response.optString("TMobile");
		this.TPos = response.optString("TPos");
		this.TName = response.optString("TName");
		this.Userid = response.optString("Userid");
		this.ClassName = response.optString("ClassName");
	}

}
