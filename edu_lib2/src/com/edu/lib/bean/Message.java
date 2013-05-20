package com.edu.lib.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class Message implements Serializable{
	private static final long serialVersionUID = 4719470393825138685L;
	public final static int INBOX  = 1; //1是接收到的
	public final static int SENT  = 2; // 2是发出的
	
	public int message_thread_id;// messsage 表中的 thread id  属于哪个会话
	public int thread_id;// thread 表中的 id
	public int type = INBOX;// 类型 1是接收到的，2是发出的
	public String PID;// 消息id
	public String SendID;// 发送人id
	public String SendName;// 发送人名称
	public String ReceiverID;// 接收人id
	public String ReceiverName;// 发送人名称
	public String Title;// 标题
	public String Content;// 内容
	public String SendTime;// 发送时间
	public String ParentID;// 上级消息id(新建的为0)
	public int message_count;// 消息条数
	
	public boolean isNew;// 是否是新消息
	
	public Message(){}
	
	// pmss
	public Message(JSONObject response){
		this.PID = response.optString("PID");
		this.SendID = response.optString("SendID");
		this.SendName = response.optString("SendName");
		this.ReceiverID = response.optString("ReceiverID");
		this.ReceiverName = response.optString("ReceiverName");
		this.Title = response.optString("Title");
		this.Content = response.optString("Content");
		this.SendTime = response.optString("SendTime");
		this.ParentID = response.optString("ParentID");
	}
	
//	public static Message copy(Message m){
//		if(m == null) return null;
//		Message message = new Message();
//		message.PID = m.PID;
//		message.SendID = m.SendID;
//		message.SendName = m.SendName;
//		message.Title = m.Title;
//		message.Content = m.Content;
//		message.ParentID = m.ParentID;
//		return message;
//	}
}
