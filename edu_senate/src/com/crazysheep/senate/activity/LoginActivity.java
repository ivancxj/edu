package com.crazysheep.senate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.crazysheep.senate.R;

public class LoginActivity extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			Intent intent = new Intent(this,TabHostActivity.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}
		
	}

}
