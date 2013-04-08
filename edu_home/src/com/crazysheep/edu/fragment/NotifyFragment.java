package com.crazysheep.edu.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.crazysheep.edu.R;
import com.crazysheep.edu.activity.FragmentChangeActivity;
import com.crazysheep.edu.fragment.adapter.NotifyFragmentAdapter;
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

//    public static NotifyFragment newInstance(){
//        NotifyFragment fragment = new NotifyFragment();
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notify, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new NotifyFragmentAdapter(getChildFragmentManager());

        mPager = (ViewPager) getView().findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) getView().findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) { }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ((FragmentChangeActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        break;
                    default:
                        ((FragmentChangeActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        break;
                }
            }

        });

        float density = getResources().getDisplayMetrics().density;
        mIndicator.setRadius(4 * density);
        mIndicator.setPageColor(0xFFFFFFFF);
        mIndicator.setFillColor(Color.rgb(11, 12, 0));


    }
}
