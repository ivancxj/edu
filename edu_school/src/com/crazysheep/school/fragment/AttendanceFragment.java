package com.crazysheep.school.fragment;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazysheep.school.R;
import com.crazysheep.school.activity.AttendanceDetailActivity;
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
public class AttendanceFragment extends Fragment implements OnClickListener{

	private TextView record;//出勤人数
	private TextView notrecord;//缺勤人数
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_attendance, container, false);
		record = (TextView)view.findViewById(R.id.attendance_record);
		notrecord = (TextView)view.findViewById(R.id.attendance_notrecord);
		view.findViewById(R.id.attendance_detail).setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getGardenRecord();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.attendance_detail:
			Intent intent = new Intent(getActivity(),AttendanceDetailActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
	private void getGardenRecord() {
		final ProgressDialog progress = UIUtils
				.newProgressDialog(getActivity(), "请稍等..");
		JsonHandler handler = new JsonHandler(getActivity()) {
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
				record.setText(response.optInt("Recorded")+"");
				notrecord.setText(response.optInt("NotRecord")+"");
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetGardenRecord(user.gardenID, handler);
	}
}
