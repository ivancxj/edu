package com.crazysheep.edu.fragment.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.crazysheep.edu.fragment.AlbumFragment;
import com.edu.lib.bean.Album;

public class AlbumFragmentAdapter extends FragmentStatePagerAdapter{
	
	public ArrayList<Album> user_albums = null;
	public ArrayList<Album> class_albums = null;
	
	public AlbumFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if(position == 0 && user_albums != null){
			fragment = AlbumFragment.newInstance(user_albums, true);
		}else if(position == 1 && class_albums != null){
			fragment = AlbumFragment.newInstance(class_albums, false);
		}
		
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}
}
