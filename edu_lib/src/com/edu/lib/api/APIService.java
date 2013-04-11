package com.edu.lib.api;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APIService {
	private final static String HOST = "http://sysmanage.youjiaoyun.net/photo.asmx";
	private final static String URL_SPLITTER = "/";
	private final static String URL_API_HOST = HOST + URL_SPLITTER;

	/*****************************************
	 * 暂时用不到接口
	 ****************************************/
	private final static String GetFriendAlbum = URL_API_HOST
			+ "GetFriendAlbum";
	private final static String UpdateAlbumInfo = URL_API_HOST
			+ "UpdateAlbumInfo";

	/*****************************************
	 * 公共接口
	 ****************************************/
	private final static String CheckLogin = URL_API_HOST + "CheckLogin";
	private final static String GetAlbumPhotoList = URL_API_HOST
			+ "GetAlbumPhotoList";
	private final static String UploadAlbumPhotos = URL_API_HOST
			+ "UploadAlbumPhotos";

	/*****************************************
	 * 家务通（家长用的）
	 ****************************************/
	// 获取个人（班级）相册信息
	private final static String GetUserAlbum = URL_API_HOST + "GetUserAlbum";
	private final static String AddUserAlbum = URL_API_HOST + "AddUserAlbum";
	// 评论
	private final static String SendPhotoAlbumForum = URL_API_HOST
			+ "SendPhotoAlbumForum";
	// 获取评论列表
	private final static String GetPhotoAlbumForum = URL_API_HOST
			+ "GetPhotoAlbumForum";

	/*****************************************
	 * 教务通（老师用的）
	 ****************************************/
	private final static String AddClassAlbum = URL_API_HOST + "AddClassAlbum";
	private final static String GetClassAlbum = URL_API_HOST + "GetClassAlbum";

	// 获取班级公告信息
	private final static String GetClassAnnouncement = URL_API_HOST
			+ "GetClassAnnouncement";
	// 添加班级公告信息
	private final static String SendClassAnnouncement = URL_API_HOST
			+ "SendClassAnnouncement";

	// 获取学生出勤列表
	private final static String GetStudentCardRecords = URL_API_HOST
			+ "GetStudentCardRecords";
	// 修改学生出勤状态
	private final static String UpdateStudentCardRecord = URL_API_HOST
			+ "UpdateStudentCardRecord";

	/*****************************************
	 * 园长通（园长用）
	 ****************************************/
	// 查看园所出勤列表
	private final static String GetGardenRecord = URL_API_HOST
			+ "GetGardenRecord";
	// 查看教师缺勤详情 暂时不做
	// 获取未考勤学生列表
	private final static String GetStudentNoRecords = URL_API_HOST
			+ "GetStudentNoRecords";

	// 获取园区公告信息
	private final static String GetGardenAnnouncement = URL_API_HOST
			+ "GetGardenAnnouncement";
	// 添加园区公告信息
	private final static String SendGardenAnnouncement = URL_API_HOST
			+ "SendGardenAnnouncement";

	// 获取老师列表
	private final static String GetTeacherList = URL_API_HOST
			+ "GetTeacherList";

	/*****************************************
	 * 暂时用不到接口
	 ****************************************/
	public static void GetFriendAlbum(String classid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("classid", classid);
		get(GetFriendAlbum, params, handler);
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

	/*****************************************
	 * 公共接口
	 ****************************************/
	/**
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
	 * ok 获取个人/班级相册照片列表
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
	 * ok 上传相册照片
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

	/*****************************************
	 * 家务通（家长用的） TODO
	 ****************************************/
	/**
	 * ok 获取个人（班级）相册信息
	 * 
	 * @param owner
	 *            所属用户
	 */
	public static void GetUserAlbum(String owner, String classid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("owner", owner);
		params.put("classid", classid);
		get(GetUserAlbum, params, handler);
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
		params.put("sdesc", sdes);
		post(AddUserAlbum, params, handler);
	}

	/*****************************************
	 * 教务通（老师用的）TODO
	 ****************************************/

	/**
	 * 
	 * @param albumID
	 *            相册id
	 * @param photoname
	 * 
	 * @param userid
	 *            用户id
	 * @param forumid
	 *            评论者id
	 * @param Content
	 *            评论内容
	 */
	public static void SendPhotoAlbumForum(String albumID, String photoname,
			String formuserid, String Content, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("albumID", albumID);
		params.put("photoname", photoname);
		params.put("formuserid", formuserid);
		params.put("title", "");
		params.put("Content", Content);
		post(SendPhotoAlbumForum, params, handler);
	}

	/**
	 * 查看评论列表
	 * 
	 * @param albumID
	 * @param photoname
	 */
	public static void GetPhotoAlbumForum(String albumID, String photoname,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("albumID", albumID);
		params.put("photoname", photoname);
		post(GetPhotoAlbumForum, params, handler);
	}

	/**
	 * 获取班级最新的5条公告信息
	 * 
	 * @param gid
	 *            园区ID
	 * @param classid
	 * @param userid
	 */
	public static void GetClassAnnouncement(String gid, String classid,
			String userid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);
		params.put("classid", classid);
		params.put("uid", userid);
		get(GetClassAnnouncement, params, handler);
	}

	/**
	 * 添加公告信息
	 * 
	 * @param gid
	 *            园区id
	 * @param classid
	 *            班级id
	 * @param userid
	 *            用户id
	 * @param title
	 *            公告标题
	 * @param contents
	 *            公告内容
	 */
	public static void SendClassAnnouncement(String gid, String classid,
			String userid, String title, String contents,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);
		params.put("classid", classid);
		params.put("userid", userid);
		params.put("title", title);
		params.put("contents", contents);
		post(SendClassAnnouncement, params, handler);
	}

	/**
	 * 获取学生出勤列表
	 * 
	 * @param gid
	 *            园区id
	 * @param classid
	 *            班级id
	 * @param userid
	 *            用户id
	 */
	public static void GetStudentCardRecords(String gid, String classid,
			String userid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);
		params.put("classid", classid);
		params.put("userid", userid);
		post(GetStudentCardRecords, params, handler);
	}
	/**
	 * 修改学生出勤状态
	 * @param Id 考勤id
	 */
	public static void UpdateStudentCardRecord(String gid,String Userid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);
		params.put("Userid", Userid);
		post(UpdateStudentCardRecord, params, handler);
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
		params.put("sdesc", sdes);
		params.put("classid", classid);

		post(AddClassAlbum, params, handler);
	}

	/*****************************************
	 * 园长通（园长用）TODO
	 ****************************************/
	/**
	 * 查看园所出勤列表
	 * 
	 * @param gid
	 *            园区ID
	 */
	public static void GetGardenRecord(String gid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);

		get(GetGardenRecord, params, handler);
	}

	// 查看教师缺勤详情 暂时不做
	//
	/**
	 * 获取未考勤学生列表
	 * 
	 * @param gid
	 *            园区ID
	 */
	public static void GetStudentNoRecords(String gid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);

		get(GetStudentNoRecords, params, handler);
	}

	/**
	 * 获取园区公告信息
	 * 
	 * @param gid
	 *            园区ID
	 */
	public static void GetGardenAnnouncement(String gid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);

		get(GetGardenAnnouncement, params, handler);
	}

	/**
	 * 添加园区公告信息
	 * 
	 * @param gid
	 *            园区id
	 * @param classid
	 *            班级id
	 * @param userid
	 *            用户id
	 * @param title
	 *            公告标题
	 * @param contents
	 *            (非必填) 公告内容
	 */
	public static void SendGardenAnnouncement(String gid, String classid,
			String userid, String title, String contents,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);
		params.put("classid", classid);
		params.put("userid", userid);
		params.put("title", title);
		params.put("contents", contents);

		post(SendGardenAnnouncement, params, handler);
	}

	/**
	 * 获取老师列表
	 * 
	 * @param gid
	 *            园区id
	 */
	public static void GetTeacherList(String gid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("gid", gid);

		get(GetTeacherList, params, handler);
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
