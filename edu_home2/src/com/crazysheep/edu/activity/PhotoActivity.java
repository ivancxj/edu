package com.crazysheep.edu.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.crazysheep.edu.R;
import com.crazysheep.edu.fragment.PhotoFragment;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.Photo;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.CommonUtils;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * 查看图片
 * 
 * @author ivan
 */
public class PhotoActivity extends ActionBarActivity implements OnClickListener {

	private ImagePagerAdapter mAdapter;
	private ViewPager mPager;

	private String albumID;
	private ArrayList<Photo> photos;

	private final static String EXTRA_ALBUM_ID = "extra_album_id";
	private final static String EXTRA_CURRENT_ITEM = "extra_current_item";
	private final static String EXTRA_PHOTOS = "extra_photos";

	public static void startActivity(Context context, String albumID,
			int position, ArrayList<Photo> photos) {
		Intent intent = new Intent(context, PhotoActivity.class);
		intent.putExtra(EXTRA_ALBUM_ID, albumID);
		intent.putExtra(EXTRA_CURRENT_ITEM, position);
		intent.putExtra(EXTRA_PHOTOS, photos);
		context.startActivity(intent);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_photo);
		setTitle("详情");
		setHomeActionListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		albumID = getIntent().getStringExtra(EXTRA_ALBUM_ID);
		photos = (ArrayList<Photo>) getIntent().getSerializableExtra(
				EXTRA_PHOTOS);

		mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(1);
		mPager.setAdapter(mAdapter);

		final int extraCurrentItem = getIntent().getIntExtra(
				EXTRA_CURRENT_ITEM, -1);
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);
		}
	}

	private class ImagePagerAdapter extends FragmentPagerAdapter {

		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public Fragment getItem(int position) {
			return PhotoFragment.newInstance(albumID, photos.get(position));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.comment_count:// 查看评论
		// PhotoFragment fragment = (PhotoFragment)mAdapter.getItem(
		// mPager.getCurrentItem());
		// CommentActivity.startActivity(this, fragment.comments);
		// break;
		// 发表评论
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

		final ProgressDialog progress = UIUtils.newProgressDialog(this,
				"提交评论中...");
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

				edit.setText("");
				PhotoFragment fragment = (PhotoFragment) mAdapter.instantiateItem(mPager, mPager.getCurrentItem());
				fragment.addCount();
				CommonUtils.hideInputKeyboard(PhotoActivity.this,
						edit.getWindowToken());

				UIUtils.showToast(PhotoActivity.this, "评论成功");
			}
		};
		User user = AppConfig.getAppConfig(this).getUser();
		Photo photo = photos.get(mPager.getCurrentItem());
		APIService.SendPhotoAlbumForum(albumID, photo.Name, user.memberid, edit
				.getText().toString(), handler);
	}
}
