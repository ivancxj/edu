package com.edu.lib.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APIService {
	private final static String HOST = "http://sysmanage.youjiaoyun.net/photo.asmx";
	private final static String URL_SPLITTER = "/";
	private final static String URL_API_HOST = HOST + URL_SPLITTER;

	private final static String AddClassAlbum = URL_API_HOST + "AddClassAlbum";
	private final static String AddUserAlbum = URL_API_HOST + "AddUserAlbum";
	private final static String CheckLogin = URL_API_HOST + "CheckLogin";
	private final static String GetClassAlbum = URL_API_HOST + "GetClassAlbum";
	private final static String GetClassAnnouncement = URL_API_HOST
			+ "GetClassAnnouncement";
	private final static String GetFriendAlbum = URL_API_HOST
			+ "GetFriendAlbum";
	private final static String GetUserAlbum = URL_API_HOST + "GetUserAlbum";
	private final static String GetAlbumPhotoList = URL_API_HOST
			+ "GetAlbumPhotoList";
	private final static String SendClassAnnouncement = URL_API_HOST
			+ "SendClassAnnouncement";
	private final static String UpdateAlbumInfo = URL_API_HOST
			+ "UpdateAlbumInfo";
	private final static String UploadAlbumPhotos = URL_API_HOST
			+ "UploadAlbumPhotos";

	/**
	 * 上传班级相册照片
	 * 
	 * @param albumID
	 *            相册id
	 * @param gid
	 *            园区id
	 * @param classid
	 *            班级id
	 * @param userid
	 *            用户id
	 * @param resume_file
	 *            图片文件
	 */
	public static void UploadAlbumPhotos(String albumID, String gid,
			String classid, String userid, String resume_file,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("albumID", albumID);
		params.put("gid", gid);
		params.put("classid", classid);
		params.put("userid", userid);
		if (resume_file != null) {
			File file = new File(resume_file);
			if (file.isFile()) {
				try {
					params.put("resume_file", file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		post(UploadAlbumPhotos, params, handler);
	}

	/**
	 * 修改相册信息
	 * 
	 * @param albumID
	 *            相册id
	 * @param sname
	 *            相册名称
	 * @param sdes
	 *            相册
	 */
	public static void UpdateAlbumInfo(String albumID, String sname,
			String sdes, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("albumID", albumID);
		params.put("sname", sname);
		params.put("sdes", sdes);
		post(UpdateAlbumInfo, params, handler);
	}

	// TODO
	public static void SendClassAnnouncement(AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		post(SendClassAnnouncement, params, handler);
	}

	/**
	 * 获取个人/班级相册照片列表
	 * 
	 * @param albumID
	 *            相册id
	 * @param gid
	 *            园区id
	 * @param classid
	 *            班级id
	 * @param userid
	 *            用户id
	 */
	public static void GetAlbumPhotoList(String albumID, String gid,
			String classid, String userid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("albumID", albumID);
		params.put("gid", gid);
		params.put("classid", classid);
		params.put("userid", userid);
		get(GetAlbumPhotoList, params, handler);
	}

	/**
	 * 获取个人相册信息
	 * 
	 * @param owner
	 *            所属用户
	 */
	public static void GetUserAlbum(String owner,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("owner", owner);
		get(GetUserAlbum, params, handler);
	}

	public static void GetFriendAlbum(String classid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("classid", classid);
		get(GetFriendAlbum, params, handler);
	}

	/**
	 * 获取班级最新的5条公告信息
	 * @param gid 园区ID
	 * @param classid
	 * @param userid
	 */
	public static void GetClassAnnouncement(String gid, String classid,
			String userid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);
		params.put("classid", classid);
		params.put("userid", userid);
		get(GetClassAnnouncement, params, handler);
	}

	/**
	 * ok 获取班级相册信息
	 * 
	 * @param classid
	 *            班级ID
	 */
	public static void GetClassAlbum(String classid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("classid", classid);
		get(GetClassAlbum, params, handler);
	}

	/**
	 * ok
	 * 
	 * @param uname
	 * @param pass
	 */
	public static void CheckLogin(String uname, String pass,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uname", uname);
		params.put("pass", pass);
		post(CheckLogin, params, handler);
	}

	/**
	 * 添加个人相册信息
	 * 
	 * @param userid
	 *             用户guid
	 * @param sname
	 *            主题名
	 * @param sdes
	 *            主题描述 （非必须）
	 */
	public static void AddUserAlbum(String userid, String sname, String sdes,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("userid", userid);
		params.put("sname", sname);
		params.put("sdes", sdes);
		post(AddUserAlbum, params, handler);
	}

	/**
	 * 添加班级相册信息
	 * 
	 * @param userid
	 *            用户guid
	 * @param sname
	 *            主题名
	 * @param sdes
	 *            主题描述 （非必须）
	 * @param classid
	 *            班级id
	 */
	public static void AddClassAlbum(String userid, String sname, String sdes,
			String classid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("userid", userid);
		params.put("sname", sname);
		params.put("sdes", sdes);
		params.put("classid", classid);

		post(AddClassAlbum, params, handler);
	}

	public static void cancelAllRequest(Context context) {
		client.cancelRequests(context, true);
	}

	private static AsyncHttpClient client = new AsyncHttpClient();

	private static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	private static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}
}
