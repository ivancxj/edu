package com.crazysheep.school.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazysheep.school.R;
import com.crazysheep.school.fragment.AttendanceFragment;
import com.crazysheep.school.fragment.NotifyFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class FragmentChangeActivity extends SlidingFragmentActivity implements
        View.OnClickListener {

    private Fragment mContent;
    final static int REQUEST_CREATE = 10001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");

        setContentView(R.layout.content_frame);

        if (mContent == null) {
            mContent = new AttendanceFragment();
            ((TextView) findViewById(R.id.action_title)).setText("出勤");
            findViewById(R.id.action).setOnClickListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mContent).commit();
        }


        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment(0), "menu").commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentById(R.id.content_frame).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchContent(Fragment fragment, MenuFragment.Menu menu) {
        mContent = fragment;
        getSlidingMenu().showContent();
        ((TextView) findViewById(R.id.action_title)).setText(menu.title);
        if (fragment != null) {
            if (fragment instanceof NotifyFragment) {
                setRightAction("发通知", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FragmentChangeActivity.this, CreateNotifyActivity.class);
                        startActivityForResult(intent, REQUEST_CREATE);
                    }
                });
            } else {
                findViewById(com.edu.lib.R.id.action_right).setVisibility(View.GONE);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment, fragment.getClass().toString()).commit();
        }
    }

    public void setRightAction(String title, View.OnClickListener listener) {
        ((Button) findViewById(com.edu.lib.R.id.action_right)).setText(title);
        findViewById(com.edu.lib.R.id.action_right).setOnClickListener(listener);
        findViewById(com.edu.lib.R.id.action_right).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action:
                getSlidingMenu().showMenu();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定要退出吗？");
        builder.setCancelable(true);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(1);

                    }
                });
        builder.setNegativeButton("取消", null);
        builder.show();

    }
}
