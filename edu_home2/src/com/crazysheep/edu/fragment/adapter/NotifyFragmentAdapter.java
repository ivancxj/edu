package com.crazysheep.edu.fragment.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.crazysheep.edu.fragment.NotifyF;
import com.edu.lib.bean.Announcement;

public class NotifyFragmentAdapter extends FragmentPagerAdapter{

	public ArrayList<Announcement> announcements = null;
	
	public NotifyFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if(announcements == null)
			return null;
		Announcement announcement = announcements.get(position);
		
		return NotifyF.newInstance(announcement);
	}

	@Override
	public int getCount() {
		if(announcements == null)
			return 0;
		return announcements.size();
	}

}
