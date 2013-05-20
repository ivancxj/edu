//package com.crazysheep.senate.activity;
//
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.View.OnClickListener;
//
//import com.crazysheep.senate.R;
//import com.crazysheep.senate.adapter.AlbumPagerAdapter;
//import com.crazysheep.senate.fragment.AlbumFragment;
//import com.crazysheep.senate.fragment.TopicListFragment;
//
///**
// * 相册
// * 
// * @author ivan
// * 
// */
//public class AlbumActivity extends FragmentActivity implements OnClickListener {
//	public ViewPager pager;
//	AlbumPagerAdapter adapter;
//
//	public static final String DYNAMICACTION = "com.crazysheep.senate";
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.activity_album);
//		pager = (ViewPager) findViewById(R.id.pager);
//
//		adapter = new AlbumPagerAdapter(this, pager);
//		adapter.addTab(AlbumFragment.class, null);
//		adapter.addTab(TopicListFragment.class, null);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back:
//			pager.setCurrentItem(0);
//			((TabHostActivity) getParent()).showRadio(true);
//			break;
//
//		case R.id.upload:
//			((TabHostActivity) getParent()).showRadio(false);
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	@Override
//	public void onBackPressed() {
//		if (((TabHostActivity) getParent()).isShowPhoto()) {
//			((TabHostActivity) getParent()).showRadio(true);
//		} else {
//			super.onBackPressed();
//		}
//	}
//
//}
