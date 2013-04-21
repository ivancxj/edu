package com.crazysheep.school.activity;

import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.crazysheep.school.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
import org.json.JSONObject;

/**
 * 创建通知
 *
 * @author ivan
 */
public class CreateNotifyActivity extends ActionBarActivity implements
        OnClickListener {

    TextView title;
    TextView content;
    TextView create_notify_count;
    private final static int MAX = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notify);
        bindActionBar();
        mActionBar.setTitle("发通知");
        showBackAction();

        title = (TextView) findViewById(R.id.create_notify_title);
        content = (TextView) findViewById(R.id.create_notify_content);

        create_notify_count = (TextView) findViewById(R.id.create_notify_count);
        create_notify_count.setText(MAX + "/" + MAX);
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
                create_notify_count.setText((MAX - content.getText().toString().length()) + "/" + MAX);
            }
        };

        content.addTextChangedListener(watcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                create();
                break;

            default:
                break;
        }

    }

    // 创建园区通知
    private void create() {
        if (TextUtils.isEmpty(title.getText().toString())) {
            UIUtils.showErrToast(this, "请输入通知标题");
            return;
        }

        if (TextUtils.isEmpty(content.getText().toString())) {
            UIUtils.showErrToast(this, "请输入通知内容");
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
//                UIUtils.showToast(CreateNotifyActivity.this, "发送通知成功");
//                setResult(RESULT_OK);
//                finish();
                sure();
            }
        };
        User user = AppConfig.getAppConfig(this).getUser();
        // TODO 园务通发送公告可选发给老师或者学生。选择了发送老师isteacher为true，家长isstu为true
        boolean isteacher = true;
        boolean isstu = true;
        APIService.SendGardenAnnouncement(user.gardenID, user.classID,
                user.memberid, title.getText().toString(), content.getText()
                .toString(),isteacher,isstu, handler);
    }
    
	private void sure() {
		Builder builder = new Builder(this);
		builder.setTitle("发通知成功");
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// finish();
				// System.exit(1);
				setResult(RESULT_OK);
				finish();

			}
		});
		// builder.setNegativeButton("取消", null);
		builder.show();
	}

}
