package com.edu.lib.bean;

import org.json.JSONObject;

public class Album {
	public int isopen;// 是否公开(0:否1:是)
	public int isreview;// 是否允许评论(0:否1:是)
	public int photocount;// 照片数
	public String albumID;// 主题id
	public int flowernum;// 得花数
	public String spwd;// TODO
	public int gcid;// TODO
	public String userID;// 创建者memberid
	public String cover;// 封面文件名
	public int iscomment;// 是否允许评论(0:否1:是)
	public String session;// 学期 例:2013
	public int isdefalut;// 是否默认相册(0:否1:是)
	public String sDesc;//
	public String gid;//
	public String userName;// 创建者名称
	public String sName;// 相册描述

	public boolean isNew;
	
	public Album(){
		
	}

	public Album(JSONObject response) {
		this.isopen = response.optInt("isopen");
		this.isreview = response.optInt("isreview");
		this.photocount = response.optInt("photocount");
		this.flowernum = response.optInt("flowernum");
		this.spwd = response.optString("spwd");// todo
		this.gcid = response.optInt("gcid");// todo
		this.userID = response.optString("userID");
		this.cover = response.optString("cover");
		this.isdefalut = response.optInt("isdefalut");
		this.sDesc = response.optString("sDesc");
		this.gid = response.optString("gid");
		this.userName = response.optString("userName");
		this.albumID = response.optString("albumID");
		this.sName = response.optString("sName");
		this.iscomment = response.optInt("iscomment");
		this.session = response.optString("session");
	}
}
