//package com.crazysheep.edu.activity;
//
//import android.app.Activity;
//import android.app.AlertDialog.Builder;
//import android.app.TabActivity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.RadioButton;
//import android.widget.TabHost;
//
//import com.crazysheep.edu.R;
//import com.crazysheep.edu.fragment.TopicListFragment;
//import com.edu.lib.util.TakePhotoUtils;
//
//@SuppressWarnings("deprecation")
//public class TabHostActivity extends TabActivity implements
//		OnCheckedChangeListener, View.OnClickListener {
//
//	public static final String UPLOADACTION = "com.crazysheep.edu.upload";
//
//	private TabHost mHost;
//	private TakePhotoUtils takePhoto;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_tabhost);
//
//		initRadios();
//		setupIntent();
//		takePhoto = new TakePhotoUtils(this);
//	}
//
//	private void initRadios() {
//		((RadioButton) findViewById(R.id.radio_album))
//				.setOnCheckedChangeListener(this);
//		((RadioButton) findViewById(R.id.radio_notify))
//				.setOnCheckedChangeListener(this);
//		((RadioButton) findViewById(R.id.radio_setting))
//				.setOnCheckedChangeListener(this);
//	}
//
//	private void setupIntent() {
//		this.mHost = getTabHost();
//		this.mHost.addTab(buildTabSpec("album", "album", AlbumActivity.class));
////		this.mHost
////				.addTab(buildTabSpec("notify", "notify", NotifyActivity.class));
//		this.mHost.addTab(buildTabSpec("setting", "setting",
//				SettingActivity.class));
//
//	}
//
//	public void showRadio(boolean flag) {
//		if (flag) {
//			findViewById(R.id.main_radio).setVisibility(View.VISIBLE);
//			findViewById(R.id.photo).setVisibility(View.GONE);
//		} else {
//			findViewById(R.id.main_radio).setVisibility(View.GONE);
//			findViewById(R.id.photo).setVisibility(View.VISIBLE);
//		}
//	}
//
//	private TabHost.TabSpec buildTabSpec(String tag, String indicator,
//			@SuppressWarnings("rawtypes") Class cls) {
//		return this.mHost.newTabSpec(tag).setIndicator(indicator, null)
//				.setContent(new Intent(this, cls));
//	}
//
//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		if (isChecked) {
//			switch (buttonView.getId()) {
//			case R.id.radio_album:
//				this.mHost.setCurrentTabByTag("album");
//				break;
//			case R.id.radio_notify:
//				this.mHost.setCurrentTabByTag("notify");
//				break;
//			case R.id.radio_setting:
//				this.mHost.setCurrentTabByTag("setting");
//				break;
//
//			default:
//				break;
//			}
//
//		}
//
//	}
//
//	public boolean isShowPhoto() {
//		return findViewById(R.id.photo).getVisibility() == View.VISIBLE;
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode != Activity.RESULT_OK)
//			return;
//		switch (requestCode) {
//		// 调用Gallery返回的
//		case TakePhotoUtils.PHOTO_PICKED_WITH_DATA:
//			if (data != null) {
//				System.err.println("PHOTO_PICKED_WITH_DATA");
//				sendUploadBroadcast(data.getData());
//				// takePhoto.doCropPhoto(data.getData());
//			}
//			break;
//		// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
//		case TakePhotoUtils.CAMERA_WITH_DATA:
//			Uri imageUri = takePhoto.getImageFilePath();
//			if (imageUri != null) {
//				sendUploadBroadcast(imageUri);
//				// takePhoto.doCropPhoto(imageUri);
//			}
//			break;
//		case TakePhotoUtils.PHOTO_RESULT_DATA:// 返回处理结果
//			// Bundle extras = data.getExtras();
//			// if (extras != null) {
//			// Bitmap photo = extras.getParcelable("data");
//			// ByteArrayOutputStream stream = new ByteArrayOutputStream();
//			// (0 - 100)压缩文件
//			// photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
//			// String filePath = takePhoto.saveBitmap(photo);
//			//
//			// }
//			break;
//		}
//	}
//
//	private void sendUploadBroadcast(Uri uri) {
//		Intent intent = new Intent();
//		intent.setAction(UPLOADACTION);
//		String filePath = takePhoto.getImageArgs(this,uri);
//		intent.putExtra(TopicListFragment.EXTRA_FILENAME, filePath);
//		sendBroadcast(intent);
//		showRadio(true);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.tabhost_take:// 拍照
//			takePhoto.doTakePhoto();
//			break;
//		case R.id.tabhost_pick:// 相册
//			takePhoto.doPickPhotoFromGallery();
//			break;
//		case R.id.tabhost_cancel:
//			showRadio(true);
//			break;
//
//		default:
//			break;
//		}
//
//	}
//
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			Builder builder = new Builder(this);
//			builder.setTitle("确定要退出吗？");
//			builder.setCancelable(true);
//			builder.setPositiveButton("确定",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							finish();
//
//						}
//					});
//			builder.setNegativeButton("取消", null);
//			builder.show();
//			return true;
//		}
//		return super.dispatchKeyEvent(event);
//	}
//}
