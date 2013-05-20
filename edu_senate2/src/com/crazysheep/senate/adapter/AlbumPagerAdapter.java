package com.crazysheep.senate.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class AlbumPagerAdapter extends FragmentPagerAdapter{
	private final Context mContext;
	private final ViewPager mViewPager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	final class TabInfo {
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(Class<?> _class, Bundle _args) {
			clss = _class;
			args = _args;
		}
	}

	public AlbumPagerAdapter(FragmentActivity activity, ViewPager pager) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
		mViewPager = pager;
		mViewPager.setAdapter(this);
	}
	
	public void addTab(Class<?> clss, Bundle args) {
		TabInfo info = new TabInfo(clss, args);
		mTabs.add(info);
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		
		TabInfo info = mTabs.get(position);
		Fragment fragment = Fragment.instantiate(mContext, info.clss.getName(),
				info.args);
		return fragment;
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

}
