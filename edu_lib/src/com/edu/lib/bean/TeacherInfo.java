package com.edu.lib.bean;

import org.json.JSONObject;

public class TeacherInfo {
	
	public String TMobile;
	public String TPos;// 职务
	public String TName;
	// mobileitemteachers
	public TeacherInfo(JSONObject response){
		this.TMobile = response.optString("TMobile");
		this.TPos = response.optString("TPos");
		this.TName = response.optString("TName");
	}

}
