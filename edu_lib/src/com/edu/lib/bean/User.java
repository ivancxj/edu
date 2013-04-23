package com.edu.lib.bean;

import org.json.JSONObject;

public class User {
	public String userid;// 
	public String memberid;// 用户GUID
	public String gradeType;// 年级属性
	public String gardenum;//  年级编码
	public String gardeID;//  年级id
	public String gardenID;//  班级ID
	public String cname;// 用户真名
	public String className;//  
	public String classID;//  班级id
	
	// kindergarten  园长
	// teacherleader和member 是老师
	// parents是家长
	
	public String job;
	
	public User(){}
	public User(JSONObject response){
		if(response.has("LoginInfo")){
			response = response.optJSONObject("LoginInfo");
			this.userid = response.optString("userid");
			this.memberid = response.optString("memberid");
			this.gradeType = response.optString("gradetype");
			this.gardenum = response.optString("gardenum");
			this.gardeID = response.optString("gardeid");
			this.gardenID = response.optString("gardenID");
			this.cname = response.optString("cname");
			this.className = response.optString("className");
			this.classID = response.optString("classID");
			this.job = response.optString("job");
		}
	}
	
	// kindergarten 园长
	public boolean isKindergarten(){
		if(job == null) return false;
		if(job.equals("kindergarten")) return true;
		return false;
	}
	// teacherleader和member 是老师
	public boolean isTeacher(){
		if(job == null) return false;
		if(job.equals("teacherleader")) return true;
		if(job.equals("member")) return true;
		return false;
	}
	// parents是家长
	public boolean isParents(){
		if(job == null) return false;
		if(job.equals("parents")) return true;
		return false;
	}
}
