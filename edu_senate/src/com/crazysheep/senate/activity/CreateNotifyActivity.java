package com.crazysheep.senate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.crazysheep.senate.R;
/**
 * 创建通知
 * @author ivan
 *
 */
public class CreateNotifyActivity extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_notify);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;

		default:
			break;
		}
		
	}

}
