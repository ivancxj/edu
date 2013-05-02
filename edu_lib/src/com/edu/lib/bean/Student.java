package com.edu.lib.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class Student implements Serializable{
	private static final long serialVersionUID = 722038476521121113L;
	public String Memberid;// 学生membrid
	public String SName;// 学生名称
	public String SNum;// 学生学号
	public String SID;// 学生id
	public String ClassName;
	
	public boolean isSelect;
	
	public Student(){}
	
	public Student(JSONObject response){
		this.Memberid = response.optString("Memberid");
		this.SName = response.optString("SName");
		this.SNum = response.optString("SNum");
		this.SID = response.optString("SID");
	}
}
