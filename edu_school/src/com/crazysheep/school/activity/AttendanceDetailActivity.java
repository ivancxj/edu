package com.crazysheep.school.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazysheep.school.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

public class AttendanceDetailActivity extends ActionBarActivity implements
		OnClickListener {

	LayoutInflater inflater;
	LinearLayout detail_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance_detail);
		bindActionBar();
		mActionBar.setTitle("园所出勤");
		showBackAction();
		// 获取未考勤学生列表
		getStudentNoRecords();
		inflater = LayoutInflater.from(this);
		detail_ll = (LinearLayout)findViewById(R.id.attendance_detail_ll);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	// 获取未考勤学生列表
	private void getStudentNoRecords() {
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
				JSONArray array = response.optJSONArray("norecords");
				int length = array.length();
				for (int i = 0; i < length; i++) {
					String classname = array.optJSONObject(i).optString(
							"classname");
					String studentname = array.optJSONObject(i).optString(
							"studentname");
					createLinearLayout(classname,studentname);
				}
				
				detail_ll.forceLayout();
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		APIService.GetStudentNoRecords(user.gardenID, handler);
	}

	private void createLinearLayout(String classname, String studentname) {
		LinearLayout item = (LinearLayout)inflater.inflate(R.layout.item_attendance_detail,
				null);
		TextView grade = (TextView)item.findViewById(R.id.attendance_detail_grade);
		TextView name = (TextView)item.findViewById(R.id.attendance_detail_name);
		grade.setText(classname);
		name.setText(studentname);
		
		detail_ll.addView(item);
	}
}
