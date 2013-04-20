package com.crazysheep.edu.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.crazysheep.edu.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

public class LoginActivity extends Activity implements OnClickListener {

	EditText loginName;
	EditText loginPassword;
	CheckBox loginRember;
	CheckBox loginAutologin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginName = (EditText) findViewById(R.id.login_name);
		loginPassword = (EditText) findViewById(R.id.login_password);
		loginRember = (CheckBox) findViewById(R.id.login_rember);
		loginAutologin = (CheckBox) findViewById(R.id.login_autologin);

		// 记住密码
		if (AppConfig.getAppConfig(this).isRemberPass()) {
			loginRember.setChecked(true);
			loginName.setText(AppConfig.getAppConfig(this).getLoginName());
			// sRYVHy>m6Tgx
			loginPassword.setText(AppConfig.getAppConfig(this).getLoginPass());
		}
		// 自动登陆
		if (AppConfig.getAppConfig(this).isAutoLogin()) {
			loginAutologin.setChecked(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			// check data
			if (TextUtils.isEmpty(loginName.getText().toString())) {
				UIUtils.showErrToast(this, "请输入用户名");
				return;
			}

			if (TextUtils.isEmpty(loginPassword.getText().toString())) {
				UIUtils.showErrToast(this, "请输入密码");
				return;
			}

			final ProgressDialog progress = UIUtils.newProgressDialog(this,
					"请稍等..");
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
					LogUtils.I(LogUtils.LOGIN, response.toString());
					User user = new User(response);
					AppConfig.getAppConfig(LoginActivity.this).saveUser(user);
					//  自动登陆－》记住密码
					if (loginAutologin.isChecked()) {
						loginRember.setChecked(true);
					}
					if (loginRember.isChecked()) {
						AppConfig.getAppConfig(LoginActivity.this).setLoginName(loginName.getText().toString());
						AppConfig.getAppConfig(LoginActivity.this).setLoginPass(loginPassword.getText().toString());
					}
					AppConfig.getAppConfig(LoginActivity.this).setRemberPass(
							loginRember.isChecked());
					AppConfig.getAppConfig(LoginActivity.this).setAutoLogin(
							loginAutologin.isChecked());

				    // go next activity
					Intent intent = new Intent(LoginActivity.this,
							FragmentChangeActivity.class);
					startActivity(intent);
					finish();
				}
			};
			String md5 = loginPassword.getText().toString().trim();
			APIService.CheckLogin(loginName.getText().toString().trim(),
					md5, handler);
			break;

		default:
			break;
		}
	}
}
