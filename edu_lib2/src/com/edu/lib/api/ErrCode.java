package com.edu.lib.api;

public class ErrCode {
	public final static int API_EC_UNKNOWN = 1;
	public final static int API_EC_SERVICE = 2;
	public final static int API_EC_METHOD = 3;
	public final static int API_EC_TOO_MANY_CALLS = 4;
	public final static int API_EC_BAD_IP = 5;
	public final static int API_EC_PARAM = 100;
	public final static int API_EC_APPLICATION = 101;
	public final static int API_EC_SESSIONKEY = 102;
	public final static int API_EC_CALLID = 103;
	public final static int API_EC_SIGNATURE = 104;
	public final static int API_EC_SPAM = 105;
	public final static int API_EC_REGISTER_NOT_ALLOW = 109;
	public final static int API_EC_EMAIL = 1111;

	public static String getErrMsg(int code) {
		String msg = "";
		switch (code) {
		case API_EC_UNKNOWN:
			msg = "未知错误,请重新提交";
			break;
		case API_EC_SERVICE:
			msg = "服务目前不可使用";
			break;
		case API_EC_METHOD:
			msg = "未知方法或方法内部错误";
			break;
		case API_EC_TOO_MANY_CALLS:
			msg = "整合程序已达到允许的最大同时请求数";
			break;
		case API_EC_BAD_IP:
			msg = "请求来自一个未被当前整合程序允许的远程地址";
			break;
		case API_EC_PARAM:
			msg = "指定参数不存在或不是有效参数，请检查是否有必要参数未提交，或者提交的参数值不是合法的";
			break;
		case API_EC_APPLICATION:
			msg = "所提交的api_key未关联到任何设定程序";
			break;
		case API_EC_SESSIONKEY:
			msg = "session_key已过期或失效,请重定向让用户重新登录并获得新的session_key";
			break;
		case API_EC_CALLID:
			msg = "当前会话所提交的call_id没有大于前一次的call_id";
			break;
		case API_EC_SIGNATURE:
			msg = "签名(sig)参数不正确";
			break;
		case API_EC_SPAM:
			msg = "垃圾信息";
			break;
		case API_EC_REGISTER_NOT_ALLOW:
			msg = "当前不允许注册或不满足注册条件";
			break;
		case API_EC_EMAIL:
			msg = "Email已存在或非法";
			break;

		default:
			break;
		}
		return msg;
	}
}
