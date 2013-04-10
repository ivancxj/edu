package com.crazysheep.school.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.crazysheep.school.R;
import com.crazysheep.school.activity.CreateNotifyActivity;
import com.crazysheep.school.activity.FragmentChangeActivity;
import com.crazysheep.school.fragment.adapter.NotifyFragmentAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Announcement;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
import com.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 通知
 * 
 * @author ivan
 */
public class NotifyFragment extends Fragment implements OnClickListener{

	ArrayList<Announcement> announcements = new ArrayList<Announcement>();
	
	NotifyFragmentAdapter mAdapter;
	ViewPager mPager;
	CirclePageIndicator mIndicator;

	// public static NotifyFragment newInstance(){
	// NotifyFragment fragment = new NotifyFragment();
	// return fragment;
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_notify, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView().findViewById(R.id.notify_create).setOnClickListener(this);

		mAdapter = new NotifyFragmentAdapter(getChildFragmentManager());
		mPager = (ViewPager) getView().findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) getView().findViewById(
				R.id.indicator);
		mIndicator.setViewPager(mPager);

		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
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

		});

		float density = getResources().getDisplayMetrics().density;
		mIndicator.setRadius(4 * density);
		mIndicator.setPageColor(0xFFFFFFFF);
		mIndicator.setFillColor(Color.rgb(11, 12, 0));
		
		getData();

	}

	private void getData() {
		final ProgressDialog progress = UIUtils.newProgressDialog(
				getActivity(), "请稍候...");
		JsonHandler handler = new JsonHandler(getActivity()) {
			@Override
			public void onStart() {
				super.onStart();
				UIUtils.safeShow(progress);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				UIUtils.safeDismiss(progress);
			}

			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.NOTIFY, response.toString());
				JSONArray array = response.optJSONArray("announcements");
				if(array == null)
					return;
				int length = array.length();
				for(int i=0;i<length;i++){
					Announcement announcement = new Announcement(array.optJSONObject(i));
					announcements.add(announcement);
				}
				
				mAdapter.announcements = announcements;
				mAdapter.notifyDataSetChanged();
				
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetGardenAnnouncement(user.gardenID, handler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notify_create:
			Intent intent = new Intent(getActivity(),CreateNotifyActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
}
