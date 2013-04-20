package com.crazysheep.senate.fragment;

import java.util.ArrayList;

import android.app.Activity;
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

import com.crazysheep.senate.R;
import com.crazysheep.senate.activity.CreateNotifyActivity;
import com.crazysheep.senate.activity.FragmentChangeActivity;
import com.crazysheep.senate.fragment.adapter.NotifyFragmentAdapter;
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
public class NotifyFragment extends Fragment implements OnClickListener, ViewPager.OnPageChangeListener {

	final static int REQUEST_CREATE = 10001;
	
    ArrayList<Announcement> announcements = new ArrayList<Announcement>();

    NotifyFragmentAdapter mAdapter;
    ViewPager mPager;
    CirclePageIndicator mIndicator;

    // public static NotifyFragment newInstance(){
    // NotifyFragment fragment = new NotifyFragment();
    // return fragment;
    // }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter = new NotifyFragmentAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notify, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.create).setOnClickListener(this);

        mPager = (ViewPager) getView().findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(this);

        mIndicator = (CirclePageIndicator) getView().findViewById(
                R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setOnPageChangeListener(this);

        float density = getResources().getDisplayMetrics().density;
        mIndicator.setRadius(4 * density);
        mIndicator.setPageColor(0xFFFFFFFF);
        mIndicator.setFillColor(Color.rgb(11, 12, 0));

        getData();

    }

    private void getData() {
        JsonHandler handler = new JsonHandler(getActivity()) {
            @Override
            public void onStart() {
                super.onStart();
                getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isVisible()) {
                    getView().findViewById(R.id.loading).setVisibility(View.GONE);
                }
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                LogUtils.I(LogUtils.NOTIFY, response.toString());
                JSONArray array = response.optJSONArray("announcements");
                if (array == null)
                    return;
                int length = array.length();
                announcements.clear();
                for (int i = 0; i < length; i++) {
                    Announcement announcement = new Announcement(array.optJSONObject(i));
                    announcements.add(announcement);
                }

                mAdapter.announcements = announcements;
                mAdapter.notifyDataSetChanged();

            }
        };
        User user = AppConfig.getAppConfig(getActivity()).getUser();
        APIService.GetClassAnnouncement(user.gardenID, user.classID,
                user.memberid, handler);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == REQUEST_CREATE && resultCode == getActivity().RESULT_OK){
    		getData();
    	}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                Intent intent = new Intent(getActivity(), CreateNotifyActivity.class);
                startActivityForResult(intent, REQUEST_CREATE);
                break;

            default:
                break;
        }

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
    public void onPageScrollStateChanged(int i) {
    }
}
