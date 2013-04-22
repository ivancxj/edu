package com.crazysheep.senate.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.crazysheep.senate.R;
import com.crazysheep.senate.adapter.CommentAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.Comment;
import com.edu.lib.bean.Photo;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.CommonUtils;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

public class CommentActivity extends ActionBarActivity implements
        OnClickListener {

    ListView listview;
    CommentAdapter adapter;
    ArrayList<Comment> comments = new ArrayList<Comment>();
    String albumID;
    Photo photo;
    private final static String EXTRA_COMMENTS = "extra_comments";
    private final static String EXTRA_ALBUM_ID = "extra_album_id";
    private static final String EXTRA_PHOTO_DATA = "extra_photo_data";

    public static void startActivity(Context context,
                                     ArrayList<Comment> comments, String albumID, Photo photo) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(EXTRA_COMMENTS, comments);
        intent.putExtra(EXTRA_ALBUM_ID, albumID);
        intent.putExtra(EXTRA_PHOTO_DATA, photo);
        context.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        comments = (ArrayList<Comment>) getIntent().getSerializableExtra(
                EXTRA_COMMENTS);
        albumID = getIntent().getStringExtra(EXTRA_ALBUM_ID);
        photo = (Photo) getIntent().getSerializableExtra(EXTRA_PHOTO_DATA);

        setTitle("评论");
        setHomeActionListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview = (ListView) findViewById(R.id.listview);
        adapter = new CommentAdapter(this, comments);
        listview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_publisher_submit:
                sendComment();
                break;
            default:
                break;
        }

    }

    // 发表评论
    private void sendComment() {
        final EditText edit = (EditText) findViewById(R.id.comment_publisher_edit);
        if (TextUtils.isEmpty(edit.getText().toString())) {
            UIUtils.showToast(this, "请输入评论");
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
                LogUtils.I(LogUtils.COMMENT, response.toString());
                response = response.optJSONObject("photoforuminfo");
                if (response != null) {
                    Comment comment = new Comment(response);
                    comments.add(0, comment);
                    adapter.notifyDataSetInvalidated();
                }

                edit.setText("");
                CommonUtils.hideInputKeyboard(CommentActivity.this, edit.getWindowToken());
                UIUtils.showToast(CommentActivity.this, "评论成功");
            }
        };
        User user = AppConfig.getAppConfig(this).getUser();
        APIService.SendPhotoAlbumForum(albumID, photo.Name, user.memberid, edit
                .getText().toString(), handler);
    }
}
