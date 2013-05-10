package com.crazysheep.senate.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.crazysheep.senate.R;
import com.edu.lib.MyApplication;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements OnClickListener, Handler.Callback {

    EditText loginName;
    EditText loginPassword;
    CheckBox loginRember;
    CheckBox loginAutologin;

    //PopupWindow对象
    private PopupWindow selectPopupWindow = null;
    //自定义Adapter
    private OptionsAdapter optionsAdapter = null;
    //下拉框选项数据源
    private List<User> datas = new ArrayList<User>();
    //下拉框依附组件
    private LinearLayout parent;
    //下拉框依附组件宽度，也将作为下拉框的宽度
    private int pwidth;
    //下拉箭头图片组件
    private ImageView image;
    //恢复数据源按钮
    private Button button;
    //展示所有下拉选项的ListView
    private ListView listView = null;
    //用来处理选中或者删除下拉项消息
    private Handler handler;
    //是否初始化完成标志
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginName = (EditText) findViewById(R.id.login_name);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginRember = (CheckBox) findViewById(R.id.login_rember);
        loginAutologin = (CheckBox) findViewById(R.id.login_autologin);
        // mileslyc

        // 记住密码
//		if (AppConfig.getAppConfig(this).isRemberPass()) {
//			loginRember.setChecked(true);
//			loginName.setText(AppConfig.getAppConfig(this).getLoginName());
//			loginPassword.setText(AppConfig.getAppConfig(this).getLoginPass());
//		} else {
//			AppConfig.getAppConfig(LoginActivity.this).setLoginName("");
//			AppConfig.getAppConfig(LoginActivity.this).setLoginPass("");
//		}
        // 自动登陆
//		if (AppConfig.getAppConfig(this).isAutoLogin()) {
//			loginAutologin.setChecked(true);
//		}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//		AppConfig.getAppConfig(LoginActivity.this).setRemberPass(
//				loginRember.isChecked());
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                // check network
                MyApplication appContext = (MyApplication) getApplication();
                if (!appContext.isNetworkConnected()) {
                    UIUtils.showErrToast(this, "请先检查网络");
                    return;
                }

                // check data
                if (TextUtils.isEmpty(loginName.getText().toString())) {
                    UIUtils.showErrToast(this, "请输入用户名");
                    return;
                }

                if (TextUtils.isEmpty(loginPassword.getText().toString())) {
                    UIUtils.showErrToast(this, "请输入密码");
                    return;
                }
                v.setEnabled(false);
                final ProgressDialog progress = UIUtils.newProgressDialog(this,
                        "登陆中..");
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
                        v.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        LogUtils.I(LogUtils.LOGIN, response.toString());
                        User user = new User(response);
                        if (!user.isTeacher()) {
                            UIUtils.showToast(LoginActivity.this, "用户名或密码错误");
                            return;
                        }

                        AppConfig.getAppConfig(LoginActivity.this).saveUser(user);
                        //  自动登陆－》记住密码
//					if (loginAutologin.isChecked()) {
//						loginRember.setChecked(true);
//					}
                        if (loginRember.isChecked()) {
                            AppConfig.getAppConfig(LoginActivity.this).saveOrDeleteUser(loginName.getText().toString(),
                                    loginPassword.getText().toString(), true);
                            AppConfig.getAppConfig(LoginActivity.this)
                                    .setLoginName(loginName.getText().toString());
                            AppConfig.getAppConfig(LoginActivity.this)
                                    .setLoginPass(
                                            loginPassword.getText().toString());
                        } else {
                            AppConfig.getAppConfig(LoginActivity.this).saveOrDeleteUser(loginName.getText().toString(),
                                    loginPassword.getText().toString(), false);
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
                APIService.CheckLogin(loginName.getText().toString().trim(), md5,
                        handler);
                break;

            default:
                break;
        }
    }

    /**
     * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
     * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        while (!flag) {
            initWedget();
            flag = true;
        }
    }

    /**
     * 初始化界面控件
     */
    private void initWedget() {
        //初始化Handler,用来处理消息
        handler = new Handler(this);
        //初始化界面组件
        parent = (LinearLayout) findViewById(R.id.parent);
        image = (ImageView) findViewById(R.id.btn_select);
        //获取下拉框依附的组件宽度
        int width = parent.getWidth();
        pwidth = width;
        //设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    //显示PopupWindow窗口
                    popupWindwShowing();
                }
            }
        });
        //初始化PopupWindow
        initPopuWindow();
    }

    /**
     * 初始化填充Adapter所用List数据
     */
    private void initDatas() {
        datas.clear();
        datas = AppConfig.getAppConfig(this).getUsers();
        if(null != datas && datas.size() > 0){
            loginName.setText(datas.get(0).username);
            loginPassword.setText(datas.get(0).pwd);
            loginRember.setChecked(true);
        }
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopuWindow() {
        initDatas();
        //PopupWindow浮动下拉框布局
        View loginwindow = (View) this.getLayoutInflater().inflate(R.layout.options, null);
        listView = (ListView) loginwindow.findViewById(R.id.list);
        //设置自定义Adapter
        optionsAdapter = new OptionsAdapter(this, handler, datas);
        listView.setAdapter(optionsAdapter);
        selectPopupWindow = new PopupWindow(loginwindow, pwidth, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        selectPopupWindow.setOutsideTouchable(true);
        //这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
        //没有这一句则效果不能出来，但并不会影响背景
        //本人能力极其有限，不明白其原因，还望高手、知情者指点一下
        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }


    /**
     * 显示PopupWindow窗口
     */
    public void popupWindwShowing() {
        //将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
        //这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
        //（是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
        selectPopupWindow.showAsDropDown(parent, 0, -3);
    }

    /**
     * PopupWindow消失
     */
    public void dismiss() {
        selectPopupWindow.dismiss();
    }

    /**
     * 处理Hander消息
     */
    @Override
    public boolean handleMessage(Message message) {
        Bundle data = message.getData();
        switch (message.what) {
            case 1:
                //选中下拉项，下拉框消失
                int selIndex = data.getInt("selIndex");
                loginName.setText(datas.get(selIndex).username);
                loginPassword.setText(datas.get(selIndex).pwd);
                loginRember.setChecked(true);
                dismiss();
                break;
            case 2:
                //移除下拉项数据
                int delIndex = data.getInt("delIndex");
                AppConfig.getAppConfig(this).saveOrDeleteUser(datas.get(delIndex).username, datas.get(delIndex).pwd,
                        false);
                datas.remove(delIndex);
                //刷新下拉列表
                optionsAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

}
