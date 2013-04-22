package com.edu.lib.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class Message implements Serializable{
	private static final long serialVersionUID = 4719470393825138685L;
	public String PID;// 消息id
	public String SendID;// 发送人id
	public String SendName;// 发送人名称
	public String Title;// 标题
	public String Content;// 内容
	public String SendTime;// 发送时间
	public String ParentID;// 上级消息id(新建的为0)
	
	// pmss
	public Message(JSONObject response){
		this.PID = response.optString("PID");
		this.SendID = response.optString("SendID");
		this.SendName = response.optString("SendName");
		this.Title = response.optString("Title");
		this.Content = response.optString("Content");
		this.SendTime = response.optString("SendTime");
		this.ParentID = response.optString("ParentID");
	}
}
