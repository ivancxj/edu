package com.crazysheep.edu.activity;

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
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.Message;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.UIUtils;

public class ReplyMessageActivity extends ActionBarActivity implements
		OnClickListener {
	private final static String EXTRA_MESSAGE = "extra_message";
	Message message;
	TextView time;
	TextView content;

	private final static int MAX = 70;
	private EditText message_edit_content;
	TextView message_reply_count;
	boolean refersh = false;

	public static void startActivity(Activity context, Message message,int requestCode) {
		Intent intent = new Intent(context, ReplyMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void finish() {
		if(refersh)
			setResult(RESULT_OK);
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply_message);
		// view
		time = (TextView) findViewById(R.id.message_time);
		content = (TextView) findViewById(R.id.message_content);
		message_edit_content = (EditText) findViewById(R.id.message_edit_content);
		message_reply_count = (TextView) findViewById(R.id.message_reply_count);
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
				message_reply_count.setText((MAX - message_edit_content
						.getText().toString().length())
						+ "/" + MAX);

			}
		};
		message_edit_content.addTextChangedListener(watcher);

		// data
		message = (Message) getIntent().getSerializableExtra(EXTRA_MESSAGE);
		time.setText(message.SendTime);
		content.setText(message.Content);
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
		switch (v.getId()) {
		case R.id.message_send:
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
					refersh = true;
					finish();
					// TODO
				}
			};

			User user = AppConfig.getAppConfig(this).getUser();
			APIService.SendMsg(user.userid, user.cname, message.SendID, "",
					message_edit_content.getText().toString(), handler);
			break;

		default:
			break;
		}

	}

}
