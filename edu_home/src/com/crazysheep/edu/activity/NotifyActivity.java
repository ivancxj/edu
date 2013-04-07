package com.crazysheep.edu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.crazysheep.edu.R;
import com.crazysheep.edu.fragment.adapter.NotifyFragmentAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 通知
 * 
 * @author ivan
 * 
 */
public class NotifyActivity extends FragmentActivity {
	NotifyFragmentAdapter mAdapter;
	ViewPager mPager;
	CirclePageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_notify);

		mAdapter = new NotifyFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		
		float density = getResources().getDisplayMetrics().density;
		mIndicator.setRadius(4 * density);
		mIndicator.setPageColor(0xFFFFFFFF);
		mIndicator.setFillColor(Color.rgb(11,12,0));
	}
}
