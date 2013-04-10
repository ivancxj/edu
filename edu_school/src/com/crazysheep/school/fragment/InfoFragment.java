package com.crazysheep.school.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.crazysheep.school.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.TeacherInfo;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * 园内信息
 * 
 * @author ivan
 * 
 */
public class InfoFragment extends Fragment {
	TableLayout tableLayout;
	LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_info, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inflater = LayoutInflater.from(getActivity());
		tableLayout = (TableLayout) getView().findViewById(
				R.id.info_tablelayout);
		// //全部列自动填充空白处
		tableLayout.setStretchAllColumns(true);
		getTeacherList();
	}

	private void getTeacherList() {
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
				JSONArray array = response.optJSONArray("mobileitemteachers");
				if (array == null)
					return;
				int length = array.length();
				for (int i = 0; i < length; i++) {
					TeacherInfo info = new TeacherInfo(array.optJSONObject(i));
					TableRow tableRow = (TableRow)inflater.inflate(R.layout.tablerow_info,
							null);
					TextView name = (TextView)tableRow.findViewById(R.id.row_name);
					TextView pos = (TextView)tableRow.findViewById(R.id.row_pos);
					TextView mobile = (TextView)tableRow.findViewById(R.id.row_mobile);
					name.setText(info.TName);
					pos.setText(info.TPos);
					mobile.setText(info.TMobile);
					
					// 新建的TableRow添加到TableLayout
					tableLayout.addView(tableRow, new TableLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				}
				tableLayout.forceLayout();
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetTeacherList(user.gardenID, handler);
	}
}
