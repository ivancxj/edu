package com.edu.lib.bean;

import org.json.JSONObject;

public class User {
	public String errorInfo;// 返回错误信息
	public String memberid;// 用户GUID
	public String gradeType;//
	public String gardenum;//  园区名称
	public String gardeID;//  年级id
	public String gardenID;//  园区id
	public String cname;// 用户真名
	public String className;//  
	public String classID;//  班级id
	
	public User(){}
	public User(JSONObject response){
		if(response.has("photologin")){
			response = response.optJSONObject("photologin");
			this.errorInfo = response.optString("errorInfo");
			this.memberid = response.optString("memberid");
			this.gradeType = response.optString("gradetype");
			this.gardenum = response.optString("gardenum");
			this.gardeID = response.optString("gardeid");
			this.gardenID = response.optString("gardenID");
			this.cname = response.optString("cname");
			this.className = response.optString("className");
			this.classID = response.optString("classID");
		}
	
	}
}
