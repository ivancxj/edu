package com.crazysheep.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.crazysheep.school.R;
/**
 * 园所出勤
 * @author ivan
 *
 */
public class AttendanceActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.attendance_detail:
			Intent intent = new Intent(this,AttendanceDetailActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
}
