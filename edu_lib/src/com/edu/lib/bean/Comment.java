package com.edu.lib.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class Comment implements Serializable{
	private static final long serialVersionUID = 7280298803539125460L;
	public String id;//评价id
	public String userid;//相册所有者
	public String albumid;//相册id
	public String fromuserid;//发送者
	public String cont;//内容
	public String expression;//标题
	public String intime;//评价时间
	public String photoname;
	public String UserName;
	//photoalbumforums
	public Comment(JSONObject response) {
		this.id = response.optString("id");
		this.userid = response.optString("userid");
		this.albumid = response.optString("albumid");
		this.fromuserid = response.optString("fromuserid");
		this.cont = response.optString("cont");
		this.expression = response.optString("expression");
		this.intime = response.optString("intime");
		this.photoname = response.optString("photoname");
		this.UserName = response.optString("UserName");
	}
}
