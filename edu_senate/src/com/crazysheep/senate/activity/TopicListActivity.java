package com.crazysheep.senate.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.crazysheep.senate.R;
import com.crazysheep.senate.adapter.PhotoAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.Album;
import com.edu.lib.bean.Photo;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.CommonUtils;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.TakePhotoUtils;
import com.edu.lib.util.UIUtils;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.markupartist.android.widget.ActionBar.IntentAction;

/**
 * 主题列表 相册照片列表
 * 
 * @author ivan
 * 
 */
public class TopicListActivity extends ActionBarActivity implements
		OnItemClickListener, OnClickListener {

	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private PullToRefreshGridView gridView;
	private PhotoAdapter adapter;

	public final static String EXTRA_FILENAME = "extra_filename";
	public final static String EXTRA_MSG = "extra_msg";
	public final static String EXTRA_ALBUM = "extra_album";
	private Album album;

	private TakePhotoUtils takePhoto;
	
	boolean refresh = false;

	public static void startActivity(Activity context, String msg, Album album,int requestCode) {
		Intent intent = new Intent(context, TopicListActivity.class);
		intent.putExtra(EXTRA_MSG, msg);
		intent.putExtra(EXTRA_ALBUM, album);
		context.startActivityForResult(intent, requestCode);
//		context.startActivity(intent);
	}
	
	@Override
	public void finish() {
		if(refresh)
			setResult(RESULT_OK);
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_list);
		String msg = getIntent().getStringExtra(EXTRA_MSG);
		// name.setText(msg);

		bindActionBar();
		mActionBar.setTitle(msg);
		mActionBar.addAction(new IntentAction(this, "上传", null) {
			@Override
			public void performAction(View view) {
				super.performAction(view);
				findViewById(R.id.photo).setVisibility(View.VISIBLE);
			}
		});

		album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);

		takePhoto = new TakePhotoUtils(this);

		gridView = (PullToRefreshGridView) findViewById(R.id.grid_view);
		adapter = new PhotoAdapter(this, photos);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);

		updateAlbumPhotoLis();
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		// 调用Gallery返回的
		case TakePhotoUtils.PHOTO_PICKED_WITH_DATA:
			if (data != null) {
				System.err.println("PHOTO_PICKED_WITH_DATA");
				UploadAlbumPhotos(takePhoto.getImageArgs(this, data.getData()));
				// takePhoto.doCropPhoto(data.getData());
			}
			break;
		// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
		case TakePhotoUtils.CAMERA_WITH_DATA:
			Uri imageUri = takePhoto.getImageFilePath();
			if (imageUri != null) {
				UploadAlbumPhotos(takePhoto.getImageArgs(this, imageUri));
				// takePhoto.doCropPhoto(imageUri);
			}
			break;
		case TakePhotoUtils.PHOTO_RESULT_DATA:// 返回处理结果
			// Bundle extras = data.getExtras();
			// if (extras != null) {
			// Bitmap photo = extras.getParcelable("data");
			// ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// (0 - 100)压缩文件
			// photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
			// String filePath = takePhoto.saveBitmap(photo);
			//
			// }
			break;
		}
	}

	private void UploadAlbumPhotos(String resume_file) {
//		File file = new File(resume_file);
//		if (!file.isFile()) {
//			UIUtils.showToast(this, "图片地址不对");
//			return;
//		}
//		System.err.println("resume_file=" + resume_file);
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(resume_file, options);
////		bitmap = CommonUtils.imageZoom(bitmap, 100);
////		
//
////
//		int height = options.outHeight;
//		int width = options.outWidth;
//		System.err.println("height = "+height);
//		System.err.println("width = "+width);
////		
//		options.inJustDecodeBounds = false;
//		options.inSampleSize = 2;
//		Bitmap bitmap = BitmapFactory.decodeFile(resume_file, options);
//		height = options.outHeight;
//		width = options.outWidth;
//		System.err.println("height = "+height);
//		System.err.println("width = "+width);
//		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
//		byte[] b = baos.toByteArray();
//		// 将字节换成KB
//		double mid = b.length / 1024;
//		System.err.println("mid = "+mid);
//		
//		bitmap.recycle();
//
//		if (true)
//			return;
//		Bitmap bitmap = CommonUtils.decodeSampledBitmapFromFile(resume_file,
//				600, 800);
//		System.err.println(bitmap.getHeight());
//		System.err.println(bitmap.getWidth());
		
	
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// bitmap.compress(CompressFormat.JPEG, 100, bos);
		// byte[] bitmapdata = bos.toByteArray();
		// ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

//		File f = new File(Environment.getExternalStorageDirectory()
//				+ "/Android/data/" + "1.jpg");
//		
//		if (f.exists()) {
//			f.delete();
//			try {
//				f.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		System.err.println(f.getAbsolutePath());
//		FileOutputStream fOut = null;
//		try {
//			fOut = new FileOutputStream(f);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//		try {
//			fOut.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			fOut.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		final ProgressDialog progress = UIUtils.newProgressDialog(this,
				"请稍候...");
		JsonHandler handler = new JsonHandler(this) {
			@Override
			public void onStart() {
				super.onStart();
				findViewById(R.id.photo).setVisibility(View.GONE);
				UIUtils.safeShow(progress);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				UIUtils.safeDismiss(progress);
			}

			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.UPLOAD_PHOTO, response.toString());
				Toast.makeText(TopicListActivity.this, "上传成功",
						Toast.LENGTH_SHORT).show();
				response = response.optJSONObject("photofileinfo");
				Photo photo = new Photo(response);
				photos.add(photo);
				adapter.notifyDataSetInvalidated();
				refresh = true;
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();

		// ContentResolver resolver = getActivity().getContentResolver();
		// try {
		// InputStream
		// inStream=resolver.openInputStream(Uri.parse(resume_file));
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		 APIService.UploadAlbumPhotos(album.albumID, album.gid, user.classID,
		 user.memberid, resume_file, handler);

	}

	private void updateAlbumPhotoLis() {
		final ProgressDialog progress = UIUtils
				.newProgressDialog(this, "请稍等..");
		JsonHandler handler = new JsonHandler(this) {
			@Override
			public void onStart() {
				super.onStart();
				UIUtils.safeShow(progress);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				UIUtils.safeDismiss(progress);
			}

			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.PhotoList, response.toString());
				JSONArray array = response.optJSONArray("photofileinfos");
				if (array != null) {
					int length = array.length();
					for (int i = 0; i < length; i++) {
						Photo photo = new Photo(array.optJSONObject(i));
						photos.add(photo);
					}

					adapter.notifyDataSetInvalidated();
				}
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		APIService.GetAlbumPhotoList(album.albumID, album.gid, user.classID,
				user.memberid, handler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tabhost_take:// 拍照
			takePhoto.doTakePhoto();
			break;
		case R.id.tabhost_pick:// 相册
			takePhoto.doPickPhotoFromGallery();
			break;
		case R.id.tabhost_cancel:
			findViewById(R.id.photo).setVisibility(View.GONE);
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		PhotoActivity.startActivity(this, album.albumID, position, photos);
	}

	public void transImage(String fromFile, String toFile, int width,
			int height, int quality) {
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			// 缩放图片的尺寸
			float scaleWidth = (float) width / bitmapWidth;
			float scaleHeight = (float) height / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 产生缩放后的Bitmap对象
			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmapWidth, bitmapHeight, matrix, false);
			// save file
			File myCaptureFile = new File(toFile);
			FileOutputStream out = new FileOutputStream(myCaptureFile);
			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
				out.flush();
				out.close();
			}
			if (!bitmap.isRecycled()) {
				bitmap.recycle();// 记得释放资源，否则会内存溢出
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
