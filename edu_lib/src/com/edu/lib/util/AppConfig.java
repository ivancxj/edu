package com.edu.lib.util;

import java.util.Properties;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.edu.lib.bean.User;

public class AppConfig {

	private static AppConfig appConfig;
	private SharedPreferences mPreference;
	// 存储设备的唯一标示f
	public final static String CONF_UDID_PREF_KEY = "udid_pref_key";

	public final static String CONF_REMBER_PASS = "rember_pass";
	public final static String CONF_AUTO_LOGIN = "auto_login";
	public final static String CONF_LOGIN_NAME = "login_name";
	public final static String CONF_LOGIN_PASS = "login_pass";

	public static AppConfig getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig();
			appConfig.mPreference = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		return appConfig;
	}
	
	public String getUdid() {
		return get(CONF_UDID_PREF_KEY) ;
	}

	public void setUdid(String udid) {
		set(CONF_UDID_PREF_KEY, udid);
	}
	
	public String getLoginPass() {
		return get(CONF_LOGIN_PASS) ;
	}

	public void setLoginPass(String login_pass) {
		set(CONF_LOGIN_PASS, login_pass.trim());
	}
	
	public String getLoginName() {
		return get(CONF_LOGIN_NAME) ;
	}

	public void setLoginName(String login_name) {
		set(CONF_LOGIN_NAME, login_name.trim());
	}
	
	public boolean isAutoLogin() {
		if (get(CONF_AUTO_LOGIN) == null) {
			return false;
		} else {
			return Boolean.parseBoolean(get(CONF_AUTO_LOGIN));
		}
	}

	public void setAutoLogin(Boolean auto) {
		set(CONF_AUTO_LOGIN, auto.toString());
	}

	public boolean isRemberPass() {
		if (get(CONF_REMBER_PASS) == null) {
			return false;
		} else {
			return Boolean.parseBoolean(get(CONF_REMBER_PASS));
		}
	}
	
	public void cleanLoginInfo() {
		removeProperty("user.memberid", "user.gradeType", "user.gardenum", "user.gardeID",
				"user.gardenID", "user.cname", "user.className", "user.classID");
	}
	
	public void saveUser(final User user){
		if(user == null)
			return;
		mPreference.edit()
			.putString("user.memberid", user.memberid)
			.putString("user.gradeType", user.gradeType)
			.putString("user.gardenum", user.gardenum)
			.putString("user.gardeID", user.gardeID)
			.putString("user.gardenID", user.gardenID)
			.putString("user.cname", user.cname)
			.putString("user.className", user.className)
			.putString("user.classID", user.classID)
			.commit();
	}
	public User getUser(){
		if(get("user.memberid") == null){
			return null;
		}
		User user = new User();
		user.memberid = get("user.memberid");
		user.gradeType = get("user.gradeType");
		user.gardenum = get("user.gardenum");
		user.gardeID = get("user.gardeID");
		user.gardenID = get("user.gardenID");
		user.cname = get("user.cname");
		user.className = get("user.className");
		user.classID = get("user.classID");
		return user;
	}
	
	public void set(Properties ps) {
		Set<Object> set = ps.keySet();
		for (Object key : set) {
			String value = ps.getProperty((String) key);
			set((String) key, value);
		}
	}

	public void setRemberPass(Boolean rember) {
		set(CONF_REMBER_PASS, rember.toString());
	}

	private String get(String key) {
		return mPreference.getString(key, null);
	}

	private void set(String key, String value) {
		mPreference.edit().putString(key, value).commit();
	}
	
	public void removeProperty(String... key) {
		SharedPreferences.Editor editor = mPreference.edit();
		for (String k : key)
			editor.remove(k);
		editor.commit();
	}

}
