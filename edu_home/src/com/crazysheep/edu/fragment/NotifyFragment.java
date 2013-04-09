package com.crazysheep.edu.fragment;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazysheep.edu.R;
import com.crazysheep.edu.activity.FragmentChangeActivity;
import com.crazysheep.edu.fragment.adapter.NotifyFragmentAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
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
public class NotifyFragment extends Fragment {

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
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetClassAnnouncement(user.gardenID, user.classID,
				user.memberid, handler);
	}
}
