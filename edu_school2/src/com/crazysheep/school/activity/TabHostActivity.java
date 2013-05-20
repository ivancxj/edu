package com.crazysheep.school.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.crazysheep.school.R;
import com.edu.lib.util.TakePhotoUtils;

@SuppressWarnings("deprecation")
public class TabHostActivity extends TabActivity implements
		OnCheckedChangeListener, OnClickListener {

	private TabHost mHost;
	private TakePhotoUtils takePhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabhost);

		initRadios();
		setupIntent();
		takePhoto = new TakePhotoUtils(this);
	}

	private void initRadios() {
		((RadioButton) findViewById(R.id.radio_attendance))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_notify))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_info))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_setting))
				.setOnCheckedChangeListener(this);
	}

	private void setupIntent() {
		this.mHost = getTabHost();
//		this.mHost.addTab(buildTabSpec("attendance", "attendance",
//				AttendanceActivity.class));
//		this.mHost
//				.addTab(buildTabSpec("notify", "notify", NotifyActivity.class));
//		this.mHost.addTab(buildTabSpec("info", "info", InfoActivity.class));
		this.mHost.addTab(buildTabSpec("setting", "setting",
				SettingActivity.class));

	}

	public void showRadio(boolean flag) {
		if (flag) {
			findViewById(R.id.main_radio).setVisibility(View.VISIBLE);
			findViewById(R.id.photo).setVisibility(View.GONE);
		} else {
			findViewById(R.id.main_radio).setVisibility(View.GONE);
			findViewById(R.id.photo).setVisibility(View.VISIBLE);
		}
	}

	private TabHost.TabSpec buildTabSpec(String tag, String indicator,
			@SuppressWarnings("rawtypes") Class cls) {
		return this.mHost.newTabSpec(tag).setIndicator(indicator, null)
				.setContent(new Intent(this, cls));
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_attendance:
				this.mHost.setCurrentTabByTag("attendance");
				break;
			case R.id.radio_notify:
				this.mHost.setCurrentTabByTag("notify");
				break;
			case R.id.radio_info:
				this.mHost.setCurrentTabByTag("info");
				break;
			case R.id.radio_setting:
				this.mHost.setCurrentTabByTag("setting");
				break;

			default:
				break;
			}

		}

	}

	public boolean isShowPhoto() {
		return findViewById(R.id.photo).getVisibility() == View.VISIBLE;
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
			showRadio(true);
			break;

		default:
			break;
		}

	}

	// @Override
	// public void onBackPressed() {
	// if(findViewById(R.id.photo).getVisibility() == View.VISIBLE){
	// showRadio(true);
	// }else{
	// super.onBackPressed();
	// }
	// }

}
