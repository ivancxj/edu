//package com.crazysheep.senate.activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.View.OnClickListener;
//
//import com.crazysheep.senate.R;
//import com.crazysheep.senate.fragment.adapter.NotifyFragmentAdapter;
//import com.viewpagerindicator.CirclePageIndicator;
//
///**
// * 通知
// * 
// * @author ivan
// * 
// */
//public class NotifyActivity extends FragmentActivity implements OnClickListener{
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
//}
