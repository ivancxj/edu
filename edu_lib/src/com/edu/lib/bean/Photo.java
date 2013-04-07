package com.edu.lib.bean;

// photofileinfos
public class Photo {
	public String imgUrl;
	public String Name;//图片名称
	public String FullName;//图片地址
	public String ExtName;//图片类型后缀
	public String CreateTime;//图片上传时间
	
	public Photo(String imgUrl){
		this.imgUrl = imgUrl;
	}

}
