package com.edu.lib.bean;

import org.json.JSONObject;

public class Comment {
	public String id;//评价id
	public String userid;//相册所有者
	public String albumid;//相册id
	public String fromuserid;//发送者
	public String cont;//内容
	public String expression;//标题
	public String intime;//评价时间
	
	// tmp
	public String name;

	public Comment() {
	}
	
	public Comment(JSONObject response) {
		this.id = response.optString("id");
		this.userid = response.optString("userid");
		this.albumid = response.optString("albumid");
		this.fromuserid = response.optString("fromuserid");
		this.cont = response.optString("cont");
		this.expression = response.optString("expression");
		this.intime = response.optString("intime");
	}
	// photoalbumforums
	public Comment(String name, String time, String cont) {
		this.name = name;
		this.intime = time;
		this.cont = cont;
	}

}
