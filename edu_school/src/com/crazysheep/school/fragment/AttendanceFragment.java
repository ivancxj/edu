package com.crazysheep.school.fragment;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.crazysheep.school.R;
import com.crazysheep.school.activity.AttendanceDetailActivity;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;

/**
 * 园所出勤
 * 
 * @author ivan
 */
public class AttendanceFragment extends Fragment implements OnClickListener {

	private TextView record;// 出勤人数
	private TextView notrecord;// 缺勤人数
	DatePickerDialog dialog;

	String date = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_attendance, container,
				false);
		view.findViewById(R.id.cq).setOnClickListener(this);
		record = (TextView) view.findViewById(R.id.attendance_record);
		notrecord = (TextView) view.findViewById(R.id.attendance_notrecord);
		view.findViewById(R.id.attendance_detail).setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getGardenRecord();
	}

	protected Dialog onCreateDialog() {
		// 用来获取日期和时间的
		Calendar calendar = Calendar.getInstance();

		DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month,
					int dayOfMonth) {

				System.err.println("year = " + year);
				System.err.println("month = " + (month + 1));
				System.err.println("dayOfMonth = " + dayOfMonth);
				date = year+"-"+(month+1)+"-"+dayOfMonth;
			}
		};
		Dialog dialog = new DatePickerDialog(getActivity(), dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		return dialog;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.attendance_detail:
			Intent intent = new Intent(getActivity(),
					AttendanceDetailActivity.class);
			startActivity(intent);
			break;
		case R.id.cq:
			onCreateDialog().show();
			break;
		default:
			break;
		}

	}

	private void getGardenRecord() {
		JsonHandler handler = new JsonHandler(getActivity()) {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.StudentRecord, response.toString());
				record.setText(response.optInt("Recorded") + "");
				notrecord.setText(response.optInt("NotRecord") + "");
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		// date TODO
		APIService.GetGardenRecord(user.gardenID, handler);
	}
}
