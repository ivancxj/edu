package com.edu.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.edu.lib.util.AppConfig;
import com.edu.lib.util.FileUtil;
import com.edu.lib.util.StringUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

	private static final String TAG = "AndroidUtil";

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	private static final String INVALID_ANDROIDID = "9774d56d682e549c";
	public static final DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheOnDisc().cacheInMemory().bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	@Override
	public void onCreate() {
		super.onCreate();

		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) this
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
			// limit
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				 .memoryCacheSize(memoryCacheSize)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .enableLogging() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

		Thread.setDefaultUncaughtExceptionHandler(new AppException(
				getApplicationContext()));
	}

	/**
	 * 取得设备的唯一标识
	 * 
	 * imei -> androidId -> mac address -> uuid saved in sdcard
	 * 
	 * @param context
	 * @return
	 */
	public String getUdid(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		/**
		 * Returns the unique device ID, for example, the IMEI for GSM and the
		 * MEID or ESN for CDMA phones. Return null if device ID is not
		 * available.
		 */
		String imei = telephonyManager.getDeviceId();

		if (isValidImei(imei)) {
			return imei;
		}

		String androidId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		if (!TextUtils.isEmpty(androidId)
				&& !INVALID_ANDROIDID.equals(androidId.toLowerCase(Locale
						.getDefault()))) {
			return androidId;
		}

		String macAddress = getWifiMacAddress(context);
		if (!TextUtils.isEmpty(macAddress)) {
			String udid = StringUtils.toMD5(macAddress + Build.MODEL
					+ Build.MANUFACTURER + Build.ID + Build.DEVICE);
			return udid;
		}

		return getSavedUuid();
	}

	private static List<String> INVALID_IMEIs = new ArrayList<String>();
	static {
		INVALID_IMEIs.add("358673013795895");
		INVALID_IMEIs.add("004999010640000");
		INVALID_IMEIs.add("00000000000000");
		INVALID_IMEIs.add("000000000000000");
	}

	public boolean isValidImei(String imei) {
		if (TextUtils.isEmpty(imei))
			return false;
		if (imei.length() < 10)
			return false;
		if (INVALID_IMEIs.contains(imei))
			return false;
		return true;
	}

	private static final String UDID_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/data/.taoatao_udid";

	private String getSavedUuid() {

		String udid = AppConfig.getAppConfig(this).getUdid();
		if (null != udid)
			return udid;

		File file = new File(UDID_PATH);
		if (file.exists()) {
			try {
				ArrayList<String> content = FileUtil
						.readLines(new FileInputStream(file));
				if (content.size() > 0) {
					udid = content.get(0);
					AppConfig.getAppConfig(this).setUdid(udid);
					Log.i(TAG, "Got sdcard file saved udid - " + udid);
					return udid;
				}
			} catch (FileNotFoundException e) {
			}
		}

		String name = System.currentTimeMillis() + "";
		udid = UUID.nameUUIDFromBytes(name.getBytes()).toString();
		udid = StringUtils.toMD5(udid);
		AppConfig.getAppConfig(this).setUdid(udid);

		try {
			file.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, "Create file in sdcard error", e);
			return udid;
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(udid.getBytes());
			fos.flush();
			Log.i(TAG, "Saved udid into file");
		} catch (IOException e) {
			Log.e(TAG, "write file error", e);
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}

		return udid;
	}

	public String getWifiMacAddress(final Context context) {
		try {
			WifiManager wifimanager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			String mac = wifimanager.getConnectionInfo().getMacAddress();
			if (TextUtils.isEmpty(mac))
				return null;
			return mac;
		} catch (Exception e) {
			Log.d(TAG, "Get wifi mac address error", e);
		}
		return null;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

}
