package com.crazysheep.edu.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.crazysheep.edu.R;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class ActionBarActivity extends FragmentActivity{
	protected ActionBar mActionBar;
	
	public void bindActionBar() {
		mActionBar = (ActionBar)findViewById(R.id.actionbar);
		showBackAction();
	}

	protected void showBackAction() {
		BackAction backAction = new BackAction(this,
				R.drawable.ic_menu_back_default);
		mActionBar.setHomeAction(backAction);
	}
	
	private class BackAction extends IntentAction {

		public BackAction(Context context, Integer drawable) {
			super(context, drawable, null);
		}

		@Override
		public void performAction(View view) {
			finish();
		}

	}
}
