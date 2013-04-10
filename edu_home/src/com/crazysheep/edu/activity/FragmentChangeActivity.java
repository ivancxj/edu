package com.crazysheep.edu.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class FragmentChangeActivity extends SlidingFragmentActivity implements
		View.OnClickListener {

	private Fragment mContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null) {

		}

		setContentView(R.layout.content_frame);

		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment(), "menu").commit();

		findViewById(R.id.logo).setOnClickListener(this);

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
		((ImageView) findViewById(R.id.logo)).setImageResource(menu.icon);
		if (fragment != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logo:
			getSlidingMenu().showMenu();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		Builder builder = new Builder(this);
		builder.setTitle("确定要退出吗？");
		builder.setCancelable(true);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
		builder.setNegativeButton("取消", null);
		builder.show();
		
	}
}
