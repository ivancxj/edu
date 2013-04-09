package com.crazysheep.school.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.crazysheep.school.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
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
		
		create();
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
	
	private void create() {
		final ProgressDialog progress = UIUtils
				.newProgressDialog(this, "请稍等..");
		JsonHandler handler = new JsonHandler(this) {
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
				LogUtils.I(LogUtils.StudentRecord, response.toString());
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		APIService.GetGardenRecord(user.gardenID, handler);
	}
}
