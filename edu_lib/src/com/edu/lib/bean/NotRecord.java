package com.edu.lib.bean;

import org.json.JSONObject;

public class NotRecord {
	public String gradenum;
	public String memberid;
	public String gardenID;
	public String classID;
	public String gradetype;
	public String className;
	public String gradeid;
	public String gardenName;
	public String cname;
	
	public NotRecord(JSONObject response){
		this.gradenum = response.optString("gradenum");
		this.memberid = response.optString("memberid");
		this.gardenID = response.optString("gardenID");
		this.classID = response.optString("classID");
		this.gradetype = response.optString("gradetype");
		this.className = response.optString("className");
		this.gradeid = response.optString("gradeid");
		this.gardenName = response.optString("gardenName");
		this.cname = response.optString("gradenum");
	}

}
