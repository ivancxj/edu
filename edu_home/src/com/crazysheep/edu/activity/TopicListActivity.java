package com.crazysheep.edu.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.crazysheep.edu.adapter.PhotoAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Album;
import com.edu.lib.bean.Photo;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.TakePhotoUtils;
import com.edu.lib.util.UIUtils;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * 主题列表 相册照片列表
 * 
 * @author ivan
 * 
 */
public class TopicListActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private PullToRefreshGridView gridView;
	private PhotoAdapter adapter;

	private TextView name;
	public final static String EXTRA_FILENAME = "extra_filename";
	public final static String EXTRA_MSG = "extra_msg";
	public final static String EXTRA_ALBUM = "extra_album";
	private Album album;

	private TakePhotoUtils takePhoto;

	public static void startActivity(Context context, String msg, Album album) {
		Intent intent = new Intent(context, TopicListActivity.class);
		intent.putExtra(EXTRA_MSG, msg);
		intent.putExtra(EXTRA_ALBUM, album);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_list);

		takePhoto = new TakePhotoUtils(this);

		name = (TextView) findViewById(R.id.name);

		gridView = (PullToRefreshGridView) findViewById(R.id.grid_view);
		adapter = new PhotoAdapter(this, photos);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		adapter.notifyDataSetInvalidated();

		String msg = getIntent().getStringExtra(EXTRA_MSG);
		name.setText(msg);
		album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);
		updateAlbumPhotoLis();
		testData();
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
		final ProgressDialog progress = UIUtils.newProgressDialog(this,
				"请稍候...");
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
				LogUtils.I(LogUtils.UPLOAD_PHOTO, response.toString());
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
		JsonHandler handler = new JsonHandler(this) {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.PhotoList, response.toString());
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		APIService.GetAlbumPhotoList(album.albumID, album.gid, user.classID,
				user.memberid, handler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		case R.id.upload:
			findViewById(R.id.photo).setVisibility(View.VISIBLE);
			break;

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

	private void testData() {
		Photo photo = new Photo(
				"http://d01.res.meilishuo.net/pic/r/15/02/e3f9f9d4a85872b11342c4bd419e_445_650.jpeg");
		photos.add(photo);
		photo = new Photo(
				"http://imgtest-lx.meilishuo.net/pic/r/2d/2d/1b2fa1092814bbc621c13785b3f0_319_479.jpg");
		photos.add(photo);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, PhotoActivity.class);
		startActivity(intent);
	}
}
