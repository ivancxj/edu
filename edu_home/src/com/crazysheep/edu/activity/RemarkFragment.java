package com.crazysheep.edu.activity;

import java.lang.reflect.Field;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.CommonUtils;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * User: robin
 * Email: ${EMAIL}
 * Date: 13-4-22
 * Time: AM11:19
 * Package: com.crazysheep.edu.activity
 */
public class RemarkFragment extends Fragment implements OnClickListener{
	TextView name;
	TextView date;
	EditText content;
	
	// bottom
	EditText comment_publisher_edit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_remark, null);
        name = (TextView)view.findViewById(R.id.remark_name);
        date = (TextView)view.findViewById(R.id.remark_date);
        content = (EditText)view.findViewById(R.id.remark_content);
        comment_publisher_edit = (EditText)view.findViewById(R.id.comment_publisher_edit);
        comment_publisher_edit.setHint("如孩子身体状况");
        view.findViewById(R.id.comment_publisher_submit).setOnClickListener(this);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	GetSysTime();
    	User user = AppConfig.getAppConfig(getActivity()).getUser();
    	name.setText(user.cname);
    }
    
    void GetSysTime(){
    	JsonHandler handler = new JsonHandler(getActivity()){
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.GetSysTime, response.toString());
				String mdate = response.optString("ErrorInfo");
				if(!TextUtils.isEmpty(mdate)){
					String[] d = mdate.split(" ");
					if(d.length == 2){
						date.setText(d[1]);
					}
				}
				
			}
		};
		APIService.GetSysTime(handler);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送
		case R.id.comment_publisher_submit:
			String content = this.content.getEditableText().toString();
			if(TextUtils.isEmpty(content)){
				content = comment_publisher_edit.getEditableText().toString();
			}
			
			if(TextUtils.isEmpty(content)){
				UIUtils.showToast(getActivity(), "请输入内容");
				return;
			}
			
			final ProgressDialog progress = UIUtils.newProgressDialog(getActivity(),
					"请稍候...");
			JsonHandler handler = new JsonHandler(getActivity()){
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
					LogUtils.I(LogUtils.REMARK, response.toString());
					RemarkFragment.this.content.setText("");
					comment_publisher_edit.setText("");
					CommonUtils.hideInputKeyboard(getActivity(), comment_publisher_edit.getWindowToken());
					UIUtils.showToast(getActivity(), "备注成功");
				}
			};
			User user = AppConfig.getAppConfig(getActivity()).getUser();
			APIService.UpdateStudentCardRecord(user.gardenID, user.memberid, content, handler);
			
			break;

		default:
			break;
		}
		
	}
}
