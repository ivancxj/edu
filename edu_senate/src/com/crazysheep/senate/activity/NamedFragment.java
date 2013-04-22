package com.crazysheep.senate.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
	
	private void updateStudentCardRecord(Named name,final int position) {
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
				Named named = namdeds.get(position);
				response = response.optJSONObject("cardrecord");
				named.IsRecord = response.optBoolean("IsRecord");
				adapter.notifyDataSetChanged();
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		// TODO  remark
		String remark = "";
		APIService.UpdateStudentCardRecord(user.gardenID,name.Memberid,remark, handler);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
		final Named name = namdeds.get(position);
		if(!name.IsRecord){
			Builder builder = new Builder(getActivity());
			builder.setTitle("确定要修改吗？");
			builder.setCancelable(true);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateStudentCardRecord(name,position);
						}
					});
			builder.setNegativeButton("取消", null);
			builder.show();
		}else{
			UIUtils.showToast(getActivity(), "已点名，不可修改");
		}
	}

}
