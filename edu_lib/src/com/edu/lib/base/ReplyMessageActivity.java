package com.edu.lib.base;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.lib.R;
import com.edu.lib.adapter.MessageHistoryAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Message;
import com.edu.lib.bean.User;
import com.edu.lib.db.MessageHelper;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.CommonUtils;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

public class ReplyMessageActivity extends ActionBarActivity implements
		OnClickListener {
	private final static String EXTRA_MESSAGE = "extra_message";
	Message message;

//	private final static int MAX = 70;
	private EditText message_edit_content;
//	TextView message_reply_count;
	boolean refersh = false;

	MessageHelper helper;
	ListView listview;
	MessageHistoryAdapter adapter;

	public static void startActivity(Activity context, Message message,
			int requestCode) {
		Intent intent = new Intent(context, ReplyMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.startActivityForResult(intent, requestCode);
	}

	@Override
	public void finish() {
		if (refersh)
			setResult(RESULT_OK);
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply_message);
		// view
		listview =  (ListView) findViewById(R.id.message_list);
		adapter = new MessageHistoryAdapter(this);
		listview.setAdapter(adapter);
		message_edit_content = (EditText) findViewById(R.id.comment_publisher_edit);
//		message_edit_content = (EditText) findViewById(R.id.message_edit_content);
//		message_reply_count = (TextView) findViewById(R.id.message_reply_count);
//		TextWatcher watcher = new TextWatcher() {
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				message_reply_count.setText((MAX - message_edit_content
//						.getText().toString().length())
//						+ "/" + MAX);
//
//			}
//		};
//		message_edit_content.addTextChangedListener(watcher);

		// data
		message = (Message) getIntent().getSerializableExtra(EXTRA_MESSAGE);

		helper = new MessageHelper();
		helper.insert(this, Message.copy(message));
		ArrayList<Message> messages = helper.getMessages(this, message);
		adapter.add(messages);
		
		listview.setSelection(adapter.getCount() -1);

		// 设置消息为已读
		donemsg();
		setTitle(message.SendName);
		setHomeActionListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		setRightAction("删除", new OnClickListener() {
			@Override
			public void onClick(View v) {

				Builder builder = new Builder(ReplyMessageActivity.this);
				builder.setTitle("确定要删除吗？");
				builder.setCancelable(true);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								delmsg();
							}
						});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});

	}

	void donemsg() {
		JsonHandler handler = new JsonHandler(this) {
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				refersh = true;
			}
		};

		APIService.DonePms(message.PID, handler);
	}

	void delmsg() {
		final ProgressDialog progress = UIUtils.newProgressDialog(this,
				"请稍候...");
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
				finish();
				// TODO
			}
		};

		APIService.DelPms(message.PID, handler);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.comment_publisher_submit) {
			if (TextUtils.isEmpty(message_edit_content.getText().toString())) {
				UIUtils.showErrToast(this, "请输入回复内容");
				return;
			}

			final ProgressDialog progress = UIUtils.newProgressDialog(this,
					"请稍候...");
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
					LogUtils.I(LogUtils.CREATE_MESSAGE, response.toString());
					refersh = true;
					message_edit_content.setText("");
					CommonUtils.hideInputKeyboard(ReplyMessageActivity.this,
							message_edit_content.getWindowToken());
					UIUtils.showToast(ReplyMessageActivity.this, "回复成功");

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
					helper.insert(ReplyMessageActivity.this, messages);
					adapter.add(messages);
					
					listview.setSelection(adapter.getCount() -1);

				}
			};

			User user = AppConfig.getAppConfig(this).getUser();
			String pid = message.ParentID;
			if(pid.endsWith("0"))
				pid = message.PID;
			APIService.SendMsg(user.userid, user.cname, message.SendID, "",
					message_edit_content.getText().toString(),
					pid, handler);
		} else {

		}

	}

}
