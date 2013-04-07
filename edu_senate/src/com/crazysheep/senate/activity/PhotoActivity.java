package com.crazysheep.senate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.crazysheep.senate.R;
import com.crazysheep.senate.fragment.PhotoFragment;

/**
 * 查看图片
 * 
 * @author ivan
 * 
 */
public class PhotoActivity extends FragmentActivity implements OnClickListener {
	String[] imageUrls = new String[] {
			"http://d01.res.meilishuo.net/pic/r/4f/5a/bab627963cbc7b04cc3e99d38916_800_1200.c1.jpg",
			"http://d01.res.meilishuo.net/pic/r/be/b9/bf03e503ce02dd36699e9d0ed511_245_375.c1.jpg",
			"http://imgtest-lx.meilishuo.net/pic/r/1d/52/b879c5e2cdb58b1bd515360fd8c0_800_1079.c1.jpg",
			"http://imgtest.meiliworks.com/pic/r/9a/a2/580389b9f497a5043dbe89b2c3a6_228_342.c1.jpg",
			"http://imgst-dl.meilishuo.net/pic/r/58/6a/195c443d83821af99188d5f26e08_667_1000.c1.jpg" };

    private ImagePagerAdapter mAdapter;
    private ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_photo);

		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(),
				imageUrls.length);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
//		mPager.setPageMargin((int) getResources().getDimension(
//				R.dimen.image_detail_pager_margin));
		mPager.setOffscreenPageLimit(2);
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {
		private final int mSize;

		public ImagePagerAdapter(FragmentManager fm, int size) {
			super(fm);
			mSize = size;
		}

		@Override
		public int getCount() {
			return mSize;
		}

		@Override
		public Fragment getItem(int position) {
			return PhotoFragment.newInstance(imageUrls[position]);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment:
			Intent intent = new Intent(this,CommentActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
}
