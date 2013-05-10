package com.edu.lib.util;

import static java.lang.System.currentTimeMillis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;

public class TakePhotoUtils {
	/* 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识剪切图片的activity */
	public static final int PHOTO_RESULT_DATA = 3022;
	private Uri mImageFileUri;
	/* 调用的Activity */
	private Activity mActivity;
	private int mCropWidth = 0;
	private int mCropHeight = 0;
	public static final String IMAGE_UNSPECIFIED = "image/*";

	public TakePhotoUtils(Activity activity) {
		this.mActivity = activity;
	}

	public Uri getImageFilePath() {
		return mImageFileUri;
	}

	public void setCropImage(int cropWidth, int cropHeight) {
		mCropWidth = cropWidth;
		mCropHeight = cropHeight;
	}

	// ==================选择相册=================//

	/**
	 * 请求Gallery程序
	 */

	public void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			Intent intent = new Intent("android.intent.action.PICK", null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					IMAGE_UNSPECIFIED);
			mActivity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mActivity, "未找到相册集!", Toast.LENGTH_LONG).show();
		}
	}

	// ==================拍照=================//

	/**
	 * 拍照获取图片
	 */
	public void doTakePhoto() {
		try {
			if (isExternalStorageWritable()) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				mImageFileUri = getImageFileUri();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageFileUri);
				mActivity.startActivityForResult(intent, CAMERA_WITH_DATA);
			}
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mActivity, "未找到照相机!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 启动gallery去剪辑这个照片
	 * 
	 * @param uri
	 */
	public void doCropPhoto(Uri uri) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
			if (isExternalStorageWritable()) {
				intent.putExtra("crop", "true");
//				intent.putExtra("output", output);// 裁剪后图片
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				// intent.putExtra("scale", true);
				intent.putExtra("outputX", mCropWidth);
				intent.putExtra("outputY", mCropHeight);
			}
			intent.putExtra("return-data", true);
			mActivity.startActivityForResult(intent, PHOTO_RESULT_DATA);
		} catch (Exception e) {
			Toast.makeText(mActivity, "未找到照相机!", Toast.LENGTH_LONG).show();
		}
	}

	public Uri getImageFileUri() {
		// String strImgPath = Environment.getExternalStorageDirectory()
		// .toString() + "/taoatao/";// 存放照片的文件夹
		// File out = new File(strImgPath);
		// if (!out.exists()) {
		// out.mkdirs();
		// }
		// out = new File(strImgPath, getPhotoFileName() + ".jpeg");
		// Uri uri = Uri.fromFile(out);
		// return uri;
		ContentValues values = new ContentValues(4);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, getPhotoFileName());
		values.put(MediaStore.Images.Media.DESCRIPTION, "edu created");
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.ORIENTATION, 0);
		return mActivity.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	public String saveBitmap(Bitmap bitmap) {
		String strImgPath = Environment.getExternalStorageDirectory()
				.toString() + "/edu/";// 存放照片的文件夹
		File file = new File(strImgPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(strImgPath, "temp.jpeg");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 80, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	
	public static String getImageArgs(Context ctx, Uri imageUri) {
		String[] proj = { MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.ORIENTATION };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = ctx.getContentResolver().query(imageUri, proj, null,
				null, null);
		if (cursor == null) {
			return null;
		}
		// 获得用户选择的图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
		int orientation_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 获取旋转的角度
		String orientation = cursor.getString(orientation_index);
		// 最后根据索引值获取图片路径
		String filePath = cursor.getString(column_index);
		return filePath;
	}

    /*public static void setBitmap(ImageView iv, String[] args) {
		if (args == null)
			return;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// 这个isjustdecodebounds很重要
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(args[0], opt);
		// 获取到这个图片的原始宽度和高度
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		// 获取图片的宽度和高度
		DisplayMetrics displayMetrics = iv.getContext().getResources()
				.getDisplayMetrics();
		ViewGroup.LayoutParams params = iv.getLayoutParams();
		int width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getFieldValue(iv, "mMaxWidth"); // Check maxWidth parameter
		if (width <= 0)
			width = displayMetrics.widthPixels;
		int height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getFieldValue(iv, "mMaxHeight"); // Check maxHeight
														// parameter
		if (height <= 0)
			height = displayMetrics.heightPixels;
		// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
		opt.inSampleSize = 1;
		// 根据屏的大小和图片大小计算出缩放比例
		if (picWidth > picHeight) {
			if (picWidth > width)
				opt.inSampleSize = picWidth / width;
		} else {
			if (picHeight > height)
				opt.inSampleSize = picHeight / height;
		}
		// 这次再真正地生成一个有像素的，经过缩放了的bitmap
		opt.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(args[0], opt);
		// 90度图片处理
		int angle = 0;
		if (args.length > 1 && args[1] != null && !"".equals(args[1])) {
			angle = Integer.parseInt(args[1]);
		}
		if (angle != 0) {
			// 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
			Matrix m = new Matrix();
			int w = bm.getWidth();
			int h = bm.getHeight();
			m.setRotate(angle); // 旋转angle度
			bm = Bitmap.createBitmap(bm, 0, 0, w, h, m, true);// 从新生成图片
		}
		// 用imageview显示出bitmap
		iv.setImageBitmap(bm);
		iv.setTag(args[0]);
		iv.setTag(R.id.item_date, args[0]);
	}

	public static void setBitmap(ImageView iv, Uri imageUri) {
		String[] args = getImageArgs(iv.getContext(), imageUri);
		setBitmap(iv, args);
	}
*/
	private static int getFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
			L.e(e);
		}
		return value;
	}

	/**
	 * 用当前时间给取得的图片命名
	 */
	private String getPhotoFileName() {
		Date date = new Date(currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date).toString();
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the last image id from the media store
	 * 
	 * @return
	 */
	public int getLastImageId() {
		final String[] imageColumns = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
		Cursor imageCursor = mActivity.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,
				null, null, imageOrderBy);
		if (imageCursor.moveToFirst()) {
			int id = imageCursor.getInt(imageCursor
					.getColumnIndex(MediaStore.Images.Media._ID));
			String fullPath = imageCursor.getString(imageCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			imageCursor.close();
			return id;
		} else {
			return 0;
		}
	}

	public void removeImage(int id) {
		ContentResolver cr = mActivity.getContentResolver();
		cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media._ID + "=?",
				new String[] { Long.toString(id) });
	}
}
