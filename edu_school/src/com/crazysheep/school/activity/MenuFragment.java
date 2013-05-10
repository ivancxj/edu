package com.crazysheep.school.activity;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazysheep.school.R;
import com.crazysheep.school.fragment.AttendanceFragment;
import com.crazysheep.school.fragment.NotifyFragment;
import com.edu.lib.base.MessageListFragment;

@SuppressLint("ValidFragment")
public class MenuFragment extends Fragment implements View.OnClickListener {

    private Menu[] menus;
    private int defaultIndex;

    public MenuFragment() {
    }

    public MenuFragment(int defaultIndex) {
        this.defaultIndex = defaultIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] names = getResources().getStringArray(R.array.menu_names);
        TypedArray imgs = getResources().obtainTypedArray(R.array.menu_imgs);
        menus = new Menu[names.length];
        for (int i = 0; i < imgs.length(); i++) {
            int resId = imgs.getResourceId(i, -1);
            Menu menu = new Menu();
            menu.title = names[i];
            menu.icon = resId;
            menus[i] = menu;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < menus.length; i++) {
            TextView view = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.menu_item, null);
            view.setText(menus[i].title);
            view.setTag(i);
            view.setOnClickListener(this);
            view.setCompoundDrawablesWithIntrinsicBounds(menus[i].icon, 0, 0, 0);
            ((LinearLayout) getView().findViewById(android.R.id.list)).addView(view);
            if (i == defaultIndex)
                view.setSelected(true);
        }
    }

    private void switchFragment(Fragment fragment, Menu menu) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof FragmentChangeActivity) {
            FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
            fca.switchContent(fragment, menu);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < ((LinearLayout) getView().findViewById(android.R.id.list)).getChildCount(); i++) {
            ((LinearLayout) getView().findViewById(android.R.id.list)).getChildAt(i).setSelected(false);
        }
        v.setSelected(true);
        Fragment newContent = null;
        switch ((Integer) v.getTag()) {
            case 0:// 出勤
                newContent = new AttendanceFragment();
                break;
            case 1:// 通知
                newContent = new NotifyFragment();
                break;
            case 2:// 消息
                newContent = new MessageListFragment();
                break;
        }
        if (newContent != null) {
            switchFragment(newContent, menus[(Integer) v.getTag()]);
        }
    }

    public class Menu {
        public String title;
        public int icon;
    }


}
