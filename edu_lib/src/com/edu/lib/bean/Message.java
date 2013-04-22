package com.edu.lib.bean;

import org.json.JSONObject;

public class Message {
	public String PID;// 消息id
	public String SendID;// 发送人id
	public String SendName;// 发送人名称
	public String Title;// 标题
	public String Content;// 内容
	public String SendTime;// 发送时间
	
	// pmss
	public Message(JSONObject response){
		this.PID = response.optString("PID");
		this.SendID = response.optString("SendID");
		this.SendName = response.optString("SendName");
		this.Title = response.optString("Title");
		this.Content = response.optString("Content");
		this.SendTime = response.optString("SendTime");
	}
}
