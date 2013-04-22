package com.edu.lib.util;

import android.util.Log;

import com.edu.lib.BuildConfig;

public class LogUtils {
	public static final String LOGIN = "login";
	public static final String ALBUM_CLASS = "class_album";
	public static final String ALBUM_USER = "user_album";
	public static final String PhotoList = "PhotoList";
	public static final String COMMENT = "comment";
	public static final String NOTIFY = "notify";
	public static final String StudentRecord = "student_record";
	public static final String CREATE_NOTIFY = "create_notify";
	public static final String CREATE_TOPIC = "create_topic";
	public static final String UPLOAD_PHOTO = "upload_photo";
	
	public static final String MESSAGE = "message";
	public static final String CREATE_MESSAGE = "create_message";
	public static final String DELETE_MESSAGE = "delete_message";
	
	public static final String SERVER_RETURN = "server_return";
	private static final String LOG_PREFIX = "edu_";
	private static boolean isPrintLog = true;

	public static String makeLogTag(String str) {
		return LOG_PREFIX + str;
	}

	public static void I(final String tag, String message) {
		if (isPrintLog && BuildConfig.DEBUG)
			Log.i(makeLogTag(tag), message);
	}

	public static void I(final String tag, String message, Throwable cause) {
		if (isPrintLog && BuildConfig.DEBUG)
			Log.i(makeLogTag(tag), message, cause);
	}

	public static void E(final String tag, String message) {
		Log.e(makeLogTag(tag), message);
	}

	public static void E(final String tag, String message, Throwable cause) {
		Log.e(makeLogTag(tag), message, cause);
	}

	private LogUtils() {
	}
}
