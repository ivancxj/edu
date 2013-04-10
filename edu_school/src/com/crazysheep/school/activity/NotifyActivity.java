//package com.crazysheep.school.activity;
//
//import org.json.JSONObject;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.View.OnClickListener;
//
//import com.crazysheep.school.R;
//import com.crazysheep.school.fragment.adapter.NotifyFragmentAdapter;
//import com.edu.lib.api.APIService;
//import com.edu.lib.api.JsonHandler;
//import com.edu.lib.bean.User;
//import com.edu.lib.util.AppConfig;
//import com.edu.lib.util.LogUtils;
//import com.edu.lib.util.UIUtils;
//import com.viewpagerindicator.CirclePageIndicator;
//
///**
// * 通知
// * 
// * @author ivan
// * 
// */
//public class NotifyActivity extends FragmentActivity implements OnClickListener {
//	NotifyFragmentAdapter mAdapter;
//	ViewPager mPager;
//	CirclePageIndicator mIndicator;
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//
//		setContentView(R.layout.activity_notify);
//
//		mAdapter = new NotifyFragmentAdapter(getSupportFragmentManager());
//
//		mPager = (ViewPager) findViewById(R.id.pager);
//		mPager.setAdapter(mAdapter);
//
//		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
//		mIndicator.setViewPager(mPager);
//		
//		float density = getResources().getDisplayMetrics().density;
//		mIndicator.setRadius(4 * density);
//		mIndicator.setPageColor(0xFFFFFFFF);
//		mIndicator.setFillColor(Color.rgb(11,12,0));
//		
//		create();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.create:
//			Intent intent = new Intent(this,CreateNotifyActivity.class);
//			startActivity(intent);
//			break;
//
//		default:
//			break;
//		}
//		
//	}
//	
//	private void create() {
//		final ProgressDialog progress = UIUtils
//				.newProgressDialog(this, "请稍等..");
//		JsonHandler handler = new JsonHandler(this) {
//			@Override
//			public void onStart() {
//				super.onStart();
//				UIUtils.safeShow(progress);
//			}
//
//			@Override
//			public void onFinish() {
//				super.onFinish();
//				UIUtils.safeDismiss(progress);
//			}
//
//			@Override
//			public void onSuccess(JSONObject response) {
//				super.onSuccess(response);
//				LogUtils.I(LogUtils.StudentRecord, response.toString());
//			}
//		};
//		User user = AppConfig.getAppConfig(this).getUser();
//		APIService.GetGardenAnnouncement(user.gardenID, handler);
//	}
//}
