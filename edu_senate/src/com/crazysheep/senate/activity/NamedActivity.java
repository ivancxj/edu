package com.crazysheep.senate.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.GridView;

import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.*;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

import com.crazysheep.senate.R;
import com.crazysheep.senate.adapter.NamedAdapter;

/**
 * 点名
 * 
 * @author ivan
 * 
 */
public class NamedActivity extends Activity {

	ArrayList<Named> namdeds = new ArrayList<Named>();
	NamedAdapter adapter = null;
	GridView gridview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_named);
		gridview = (GridView) findViewById(R.id.gridview);
		testData();
		adapter = new NamedAdapter(this, namdeds);
		gridview.setAdapter(adapter);
		
		create();
	}

	private void testData() {
		Named named = new Named("小花", "08:30", true);
		namdeds.add(named);
		named = new Named("小花", "08:31", true);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
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
		APIService.GetStudentCardRecords(user.gardenID, user.classID,
				user.memberid, handler);
	}

}
