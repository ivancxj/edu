package com.crazysheep.edu.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.crazysheep.edu.activity.FragmentChangeActivity;
import com.crazysheep.edu.fragment.adapter.AlbumFragmentAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Album;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.widget.NonSwipeableViewPager;
import com.slidingmenu.lib.SlidingMenu;

/**
 *
 * @author ivan
 */
public class AlbumFragmentViewPager extends Fragment implements ViewPager.OnPageChangeListener, OnClickListener {

    AlbumFragmentAdapter mAdapter;
    NonSwipeableViewPager mPager;

	private ArrayList<Album> user_albums = null;
	private ArrayList<Album> class_albums = new ArrayList<Album>();
	
	private TextView cart_tabhost_todo_order;
	private TextView cart_tabhost_order;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter = new AlbumFragmentAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album_viewpager, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPager = (NonSwipeableViewPager) getView().findViewById(R.id.pager);
        mPager.setOnPageChangeListener(this);
		cart_tabhost_todo_order = (TextView) getView().findViewById(R.id.cart_tabhost_todo_order);
		cart_tabhost_todo_order.setOnClickListener(this);
		cart_tabhost_order = (TextView) getView().findViewById(R.id.cart_tabhost_order);
		cart_tabhost_order.setOnClickListener(this);
		
        getUserAlbum(); 
    }
    
	private void  getUserAlbum(){
		JsonHandler handler = new JsonHandler(getActivity()){
			@Override
			public void onStart() {
				super.onStart();
				getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				getView().findViewById(R.id.loading).setVisibility(View.GONE);
			}
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.ALBUM_USER, response.toString());
				JSONArray array = response.optJSONArray("useralbumlist");
				int length = array.length();
				user_albums = new ArrayList<Album>();
				for(int i=0;i<length;i++){
					Album album = new Album(array.optJSONObject(i));
					user_albums.add(album);
				}
				
				Album album = new Album();
				album.isNew = true;
				user_albums.add(album);
				mAdapter.user_albums = user_albums;
				
				array = response.optJSONArray("classalbumlist");
				length = array.length();
				class_albums.clear();
				for(int i=0;i<length;i++){
					album = new Album(array.optJSONObject(i));
					class_albums.add(album);
				}
				
				mAdapter.class_albums = class_albums;
				mPager.setAdapter(mAdapter);
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		if(user != null)
			APIService.GetUserAlbum(user.memberid,user.classID, handler);
	}

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                ((FragmentChangeActivity) getActivity())
                        .getSlidingMenu()
                        .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            default:
                ((FragmentChangeActivity) getActivity()).getSlidingMenu()
                        .setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cart_tabhost_todo_order:// 
			mPager.setCurrentItem(0);
			cart_tabhost_todo_order.setTextColor(getResources().getColor(
					R.color.yellow));
			cart_tabhost_order.setTextColor(getResources().getColor(
					R.color.dark_gray));

			break;
		case R.id.cart_tabhost_order:// 
			mPager.setCurrentItem(1);
			cart_tabhost_todo_order.setTextColor(getResources().getColor(
					R.color.dark_gray));
			cart_tabhost_order.setTextColor(getResources().getColor(
					R.color.yellow));
			break;

		default:
			break;
		}
	}
}
