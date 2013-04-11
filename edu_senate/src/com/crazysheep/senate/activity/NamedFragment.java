package com.crazysheep.senate.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.crazysheep.senate.R;
import com.crazysheep.senate.adapter.NamedAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Named;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * 点名
 * 
 * @author ivan
 * 
 */
public class NamedFragment extends Fragment implements OnItemClickListener{

	ArrayList<Named> namdeds = new ArrayList<Named>();
	NamedAdapter adapter = null;
	GridView gridview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_named, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gridview = (GridView) getView().findViewById(R.id.gridview);
		gridview.setOnItemClickListener(this);
		getRecords();
	}

	
	private void getRecords() {
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
				JSONArray array = response.optJSONArray("cardrecords");
				if(array == null) return;
				int length = array.length();
				for(int i = 0;i<length;i++){
					Named named = new Named(array.optJSONObject(i));
					namdeds.add(named);
				}
				
				adapter = new NamedAdapter(getActivity(), namdeds);
				gridview.setAdapter(adapter);
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetStudentCardRecords(user.gardenID, user.classID,
				user.memberid, handler);
	}
	
	private void updateStudentCardRecord(Named name) {
		final ProgressDialog progress = UIUtils.newProgressDialog(
				getActivity(), "请稍等..");
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
				UIUtils.showToast(getActivity(), "更新成功");
				// TODO
			}
		};
		APIService.UpdateStudentCardRecord(name.ID, handler);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Named name = namdeds.get(arg2);
		if(!name.change)
			updateStudentCardRecord(name);
	}

}
