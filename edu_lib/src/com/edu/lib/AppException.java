package com.edu.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

public class AppException implements UncaughtExceptionHandler {

	private final static boolean Debug = false;// 是否保存错误日志

//	private static String APPLICATION_ID = "dEtuZS1rVFB3Q195T1QzaDZZSEFVcGc6MQ";
	private static String APPLICATION_ID = "dDRxWGlVZEE1X1RZNUZvM0tvbWVFQmc6MQ";
	private static String APP_PACKAGE = "unknown";
	private static String FILES_PATH;
	private static String TAG = "CrashLog";
	private static String URL = "https://spreadsheets.google.com/formResponse?formkey=";
	private static Thread.UncaughtExceptionHandler originalExceptionHandler;
	private static String logExtension = ".dump";
	private static String crashDumpFileList[];
	private Context application;

	public AppException(Context application) {
		this.application = application;
		initCrashReporting();
		this.originalExceptionHandler = Thread
				.getDefaultUncaughtExceptionHandler();
	}

	private void initCrashReporting() {
		APP_PACKAGE = application.getPackageName();
		FILES_PATH = application.getFilesDir().getAbsolutePath();

		if (APPLICATION_ID == null || APPLICATION_ID.length() <= 0) {
			Log.d(TAG, "no application-id; not do anythings");
		} else {
			URL = URL + APPLICATION_ID;
			new Thread() {
				@Override
				public void run() {
					sendCrashDumps(application);
				};
			}.start();
		}

	}

	@SuppressWarnings("rawtypes")
	public static void sendCrashDumps(Context context) {
		
//		sendDebugReportToAuthor("1111");
		if (!isOnline(context)) {
			Log.w(TAG, "not online, we won't submit anything");
			return;
		}

		HashSet<String> hashset = new HashSet<String>();

		String as[] = findCrashDumps();
		if (as == null || as.length <= 0)
			return;

		int len = as.length;
		for (int i = 0; i < len; i++) {
			String s1 = as[i];

			BasicHttpParams basichttpparams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(basichttpparams, 5000);
			HttpConnectionParams.setSoTimeout(basichttpparams, 10000);
			DefaultHttpClient httpclient = new DefaultHttpClient(
					basichttpparams);
			HttpPost httppost = new HttpPost(URL);
			Properties properties = new Properties();
			try {
				FileInputStream fileinputstream = context.openFileInput(s1);
				properties.load(fileinputstream);
				fileinputstream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			properties.put("pageNumber", "0");
			properties.put("backupCache", "");
			properties.put("submit", "submit");
			ArrayList<BasicNameValuePair> arraylist = new ArrayList<BasicNameValuePair>();
			for (Iterator iterator = properties.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				String value = properties.getProperty(key);
				BasicNameValuePair basicnamevaluepair = new BasicNameValuePair(
						key, value);
				arraylist.add(basicnamevaluepair);
			}

			try {
				UrlEncodedFormEntity urlencodedformentity = new UrlEncodedFormEntity(
						arraylist, "UTF-8");
				httppost.setEntity(urlencodedformentity);
				Log.v(TAG, "submitting crash dump " + s1);

				HttpResponse httpresponse = httpclient.execute(httppost);
				if (httpresponse.getStatusLine().getStatusCode() == 200) {
					hashset.add(s1);

					(new File(FILES_PATH + "/" + s1)).delete();
					Log.v(TAG, "deleted crash dump " + s1);

				} else {
					String fail = httpresponse.getStatusLine().toString();
					Log.w(TAG, "failed to submit: " + fail);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String s2 = String.format("submitted %d crash dump", hashset.size());
		Log.i(TAG, s2);
	}

	private static String[] findCrashDumps() {
		String as[];
		if (crashDumpFileList != null) {
			as = crashDumpFileList;
		} else {
			File file = new File(FILES_PATH + "/");
			file.mkdir();
			MyFilter filter = new MyFilter();
			as = file.list(filter);
			crashDumpFileList = as;
		}
		return as;
	}

	public static String getOSInformation(Context context) {
		try {
			PackageManager packagemanager = context.getPackageManager();
			StringBuilder sb = new StringBuilder();
			sb.append("APP: ").append(APP_PACKAGE).append("\n");
			sb.append("Version: ");
			String s = context.getPackageName();
			String s1 = packagemanager.getPackageInfo(s, 0).versionName;
			sb.append(s1).append("\n");
			sb.append("Phone Model: ").append(Build.MODEL).append("\n");
			sb.append("OS Verison: ").append(android.os.Build.VERSION.RELEASE)
					.append("\n");
			// sb.append("Board: ").append(Build.BOARD).append("\n");
			sb.append("Device: ").append(Build.DEVICE).append("\n");
			sb.append("Display: ").append(Build.DISPLAY).append("\n");
			// sb.append("tat: ").append(Build.FINGERPRINT).append("\n");
			sb.append("Host: ").append(Build.HOST).append("\n");
			sb.append("ID: ").append(Build.ID).append("\n");
			sb.append("Model: ").append(Build.MODEL).append("\n");
			// sb.append("Product: ").append(Build.PRODUCT).append("\n");
			// sb.append("Tags: ").append(Build.TAGS).append("\n");
			// sb.append("Time: ").append(Build.TIME).append("\n");
			// sb.append("Type: ").append(Build.TYPE).append("\n");

			return sb.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		// 当前非debug模式才保存错误信息，防止调试时候乱发错误日志
		if (!BuildConfig.DEBUG) {
			StringWriter stringwriter = new StringWriter();
			PrintWriter printwriter = new PrintWriter(stringwriter);
			throwable.printStackTrace(printwriter);

			Properties properties = new Properties();
			properties.put("entry.0.single", getOSInformation(application));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date = sdf.format(new java.util.Date());
			properties.put("entry.1.single", date);
			properties.put("entry.2.single", stringwriter.toString());

			StringBuilder stringbuilder = new StringBuilder();
			stringbuilder.append(APP_PACKAGE).append("-");
			stringbuilder.append(new Random().nextInt(65535));
			stringbuilder.append(logExtension);
			try {
				FileOutputStream fileoutputstream = application.openFileOutput(
						stringbuilder.toString(), 0);
				properties.store(fileoutputstream, "");
				fileoutputstream.close();
			} catch (FileNotFoundException filenotfoundexception) {
			} catch (IOException ioexception) {
			}

			Log.d(APP_PACKAGE, stringwriter.toString());
		}

		originalExceptionHandler.uncaughtException(thread, throwable);
	}

	public static boolean isOnline(Context context) {
		NetworkInfo networkinfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		boolean flag;
		if (networkinfo != null && networkinfo.isConnectedOrConnecting()) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	private static class MyFilter implements FilenameFilter {
		@Override
		public boolean accept(File file, String s) {
			return s.endsWith(logExtension);
		}
	}

}
