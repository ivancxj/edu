package com.crazysheep.senate.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.crazysheep.senate.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * 创建主题
 *
 * @author ivan
 */
public class CreateTopicActivity extends ActionBarActivity implements
        OnClickListener {
    private final static String EXTRA_CLASS = "extra_class";
    int state = 0;

    private final static int MAX = 70;
    private EditText create_topic_name;
    private EditText create_topic_introduce;
    TextView create_topic_count;

    /**
     * @param context
     * @param state   1:person 2:class
     */
    public static void startActivity(Activity context, int state,
                                     int requestCode) {
        Intent intent = new Intent(context, CreateTopicActivity.class);
        intent.putExtra(EXTRA_CLASS, state);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        setTitle("新建主题");
        setHomeActionListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRightAction("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        state = getIntent().getIntExtra(EXTRA_CLASS, 0);
        create_topic_count = (TextView) findViewById(R.id.create_topic_count);
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
                create_topic_count.setText((MAX - create_topic_introduce.getText().toString().length()) + "/" + MAX);

            }
        };

        create_topic_name = (EditText) findViewById(R.id.create_topic_name);
        create_topic_introduce = (EditText) findViewById(R.id.create_topic_introduce);
        create_topic_introduce.addTextChangedListener(watcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (TextUtils.isEmpty(create_topic_name.getText().toString())) {
                    UIUtils.showErrToast(this, "请输入主题名");
                    return;
                }

                final ProgressDialog progress = UIUtils.newProgressDialog(this,
                        "正在创建，请稍候...");
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
                        LogUtils.I(LogUtils.CREATE_TOPIC, response.toString());
                        UIUtils.showToast(CreateTopicActivity.this, "创建成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                };
                User user = AppConfig.getAppConfig(this).getUser();
                if (state == 1) {// person
                    APIService.AddUserAlbum(user.memberid, create_topic_name
                            .getText().toString(), create_topic_introduce.getText()
                            .toString(), handler);
                } else if (state == 2) {// class
                    APIService.AddClassAlbum(user.memberid, create_topic_name
                            .getText().toString(), create_topic_introduce.getText()
                            .toString(), user.classID, handler);
                }
                break;
            default:
                break;
        }
    }
}
