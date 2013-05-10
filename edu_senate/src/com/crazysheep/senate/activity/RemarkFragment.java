package com.crazysheep.senate.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.crazysheep.senate.R;
import com.crazysheep.senate.adapter.RemarkAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.CancelFragment;
import com.edu.lib.bean.Named;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * User: robin
 * Email: ${EMAIL}
 * Date: 13-4-22
 * Time: AM11:19
 * Package: com.crazysheep.edu.activity
 */
public class RemarkFragment extends CancelFragment implements OnItemClickListener{
	
	ListView listview;
	RemarkAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_remark, null);
        listview = (ListView)view.findViewById(R.id.remark_list);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	adapter = new RemarkAdapter(getActivity());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		getRecords();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void getRecords() {
		JsonHandler handler = new JsonHandler(getActivity()) {
			@Override
			public void onStart() {
				super.onStart();
                getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				 try {
	                	getView().findViewById(R.id.loading).setVisibility(View.GONE);
					} catch (Exception e) {
					}
			}

			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.StudentRecord, response.toString());
				JSONArray array = response.optJSONArray("cardrecords");
				if(array == null) return;
				int length = array.length();
				ArrayList<Named> namdeds = new ArrayList<Named>();
				for(int i = 0;i<length;i++){
					Named named = new Named(array.optJSONObject(i));
					namdeds.add(named);
				}
				adapter.add(namdeds);
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetStudentCardRecords(user.classID,
				handler);
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
				Named named = (Named)adapter.getItem(position);
				response = response.optJSONObject("cardrecord");
				
				named.InTime = response.optString("InTime");
				named.IsRecord = response.optBoolean("IsRecord");
				// 其余的数据不返回 不要复制
//				named.SNum = response.optString("SNum");
//				named.SName = response.optString("SName");
//				named.Memberid = response.optString("Memberid");
//				named.Remark = response.optString("Remark");
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
		final Named named = (Named)adapter.getItem(position);
		if(!named.IsRecord){
			  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setTitle("给"+named.SName+"签到？");
		        builder.setCancelable(true);
		        builder.setPositiveButton("确定",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	updateStudentCardRecord(named,position);
		                    }
		                });
		        builder.setNegativeButton("取消", null);
		        builder.show();
		}
		
	}
}
