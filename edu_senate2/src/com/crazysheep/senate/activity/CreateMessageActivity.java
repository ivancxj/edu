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
import com.edu.lib.bean.Message;
import com.edu.lib.bean.Student;
import com.edu.lib.bean.User;
import com.edu.lib.db.MessageHelper2;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.CommonUtils;
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
	TextView create_message_count;
	TextView create_message_content;
	TextView create_message_name;

	ArrayList<Student> students;
	MessageHelper2 helper;
	boolean refersh = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_message);
		setTitle("发消息");
		// getTeacherList();
		setHomeActionListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		create_message_name = (TextView) findViewById(R.id.create_message_name);
		create_message_content = (TextView) findViewById(R.id.create_message_content);
		create_message_count = (TextView) findViewById(R.id.create_message_count);
		create_message_count.setText(MAX + "/" + MAX);
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
				create_message_count.setText((MAX - create_message_content
						.getText().toString().length())
						+ "/" + MAX);

			}
		};
		create_message_content.addTextChangedListener(watcher);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == REQEST_CODE && arg1 == RESULT_OK) {
			students = (ArrayList<Student>) arg2.getSerializableExtra("data");
			if (students == null)
				return;
			String txt = "";
			for (Student stu : students) {
				txt = txt + stu.SName + ",";
			}
			if (txt.endsWith(",")) {
				txt = txt.substring(0, txt.length() - 1);
			}
			create_message_name.setText("收件人: " + txt);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_message_send:
			sendmsg();
			break;
		case R.id.create_message_add:
			ContactActivity.startActivity(this, REQEST_CODE);

			break;

		default:
			break;
		}

	}

	void sendmsg() {
		if (students == null || students.size() == 0) {
			UIUtils.showToast(this, "请先选择收件人");
			return;
		}

		if (TextUtils.isEmpty(create_message_content.getEditableText()
				.toString())) {
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
				refersh = true;
				LogUtils.I(LogUtils.CREATE_MESSAGE, response.toString());
				UIUtils.showToast(CreateMessageActivity.this, "发送成功");
				create_message_content.setText("");
				CommonUtils.hideInputKeyboard(CreateMessageActivity.this,
						create_message_content.getWindowToken());

				// 插入本地
				JSONArray array = response.optJSONArray("pmss");
				if (array == null)
					return;
				ArrayList<Message> messages = new ArrayList<Message>();
				int length = array.length();
				for (int i = 0; i < length; i++) {
					Message message = new Message(array.optJSONObject(i));
					message.type = 2;
					messages.add(message);
				}
				if (helper == null)
					helper = new MessageHelper2();
				helper.insert(CreateMessageActivity.this, messages);

			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		String ids = "";
		for (Student stu : students) {
			ids = ids + stu.UserID + ",";
		}
		if (ids.endsWith(",")) {
			ids = ids.substring(0, ids.length() - 1);
		}
		LogUtils.D("ids="+ids);
		APIService.SendMsg(user.userid, user.cname, ids, "",
				create_message_content.getEditableText().toString(), "0",
				handler);
	}
	
	@Override
	public void finish() {
		if (refersh)
			setResult(RESULT_OK);
		super.finish();
	}
}
