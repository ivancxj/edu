package com.edu.lib.bean;

import java.io.Serializable;

import org.json.JSONObject;

// photofileinfos
public class Photo implements Serializable{
	private static final long serialVersionUID = 4947650859787980807L;
	public String Name;//图片名称
	public String FullData;
	public String ExtName;//图片类型后缀
	public String FileUrl;
	public String FullName;//图片地址
	public String ThumbnailWebUrl;//缩略图
	public String CreateTime;//图片上传时间
	
	public Photo(JSONObject response){
		this.Name = response.optString("Name");
		this.FullData = response.optString("FullData");
		this.ExtName = response.optString("ExtName");
		this.FileUrl = response.optString("FileUrl");
		this.FullName = response.optString("FullName");
		this.ThumbnailWebUrl = response.optString("ThumbnailWebUrl");
		this.CreateTime = response.optString("CreateTime");
	}

}
