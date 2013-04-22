package com.crazysheep.senate.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.crazysheep.senate.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.Person;
import com.edu.lib.bean.TeacherInfo;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * 发消息
 * 
 * @author ivan
 * 
 */
public class CreateMessageActivity extends ActionBarActivity implements
		OnClickListener {

	private final static int REQEST_CODE = 100;

	private final static int MAX = 70;
	TextView create_topic_count;
	TextView content;
	TextView create_message_name;

	String[] datas;
	ArrayList<Person> pesons = new ArrayList<Person>();
	Person person;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_message);
		setTitle("发消息");
		getTeacherList();
		setHomeActionListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		create_message_name = (TextView) findViewById(R.id.create_message_name);
		content = (TextView) findViewById(R.id.create_notify_content);
		create_topic_count = (TextView) findViewById(R.id.create_notify_count);
		create_topic_count.setText(MAX + "/" + MAX);
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				create_topic_count.setText((MAX - content.getText().toString()
						.length())
						+ "/" + MAX);

			}
		};
		content.addTextChangedListener(watcher);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == REQEST_CODE && arg1 == RESULT_OK) {
			person = (Person) arg2.getSerializableExtra("data");
			create_message_name.setText("收件人: " + person.name);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
			create();
			break;
		case R.id.add:
			ContactActivity.startActivity(this, pesons, REQEST_CODE);

			break;

		default:
			break;
		}

	}

	void sendmsg() {
		if (person == null) {
			UIUtils.showToast(this, "请先选择");
			return;
		}

		if (TextUtils.isEmpty(content.getEditableText().toString())) {
			UIUtils.showToast(this, "请先输入内容");
			return;
		}
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
		APIService.SendMsg(user.userid, user.cname, person.id, "", content
				.getEditableText().toString(), handler);
	}

	private void getTeacherList() {
		JsonHandler handler = new JsonHandler(this) {
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
				JSONArray array = response.optJSONArray("mobileitemteachers");
				if (array == null)
					return;
				int length = array.length();
				// datas = new String[length];
				for (int i = 0; i < length; i++) {
					TeacherInfo info = new TeacherInfo(array.optJSONObject(i));
					Person person = new Person();
					person.id = info.TID;
					person.name = info.TName;
					pesons.add(person);
				}
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		APIService.GetTeacherList(user.gardenID, handler);
	}

	private void create() {
		if (TextUtils.isEmpty(content.getText().toString())) {
			UIUtils.showErrToast(this, "请输入内容");
			return;
		}
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
				LogUtils.I(LogUtils.CREATE_NOTIFY, response.toString());
				// UIUtils.showToast(CreateNotifyActivity.this, "发通知成功");
				// setResult(RESULT_OK);
				// finish();
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		// APIService.SendClassAnnouncement(user.gardenID, user.classID,
		// user.memberid, title.getText().toString(), content.getText()
		// .toString(), handler);
	}

}
