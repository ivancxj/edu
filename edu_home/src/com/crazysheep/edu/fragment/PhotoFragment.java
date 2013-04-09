package com.crazysheep.edu.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.crazysheep.edu.activity.CommentActivity;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Comment;
import com.edu.lib.bean.Photo;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

// 相册详情
public class PhotoFragment extends Fragment implements OnClickListener {

	private final static String EXTRA_ALBUM_ID = "extra_album_id";
	private static final String EXTRA_PHOTO_DATA = "extra_photo_data";
	private String albumID;
	private Photo photo;
	private ImageView mImageView;
	private TextView count;

	private ArrayList<Comment> comments = new ArrayList<Comment>();

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory().cacheOnDisc().build();

	public static PhotoFragment newInstance(String albumID, Photo photo) {
		final PhotoFragment f = new PhotoFragment();

		final Bundle args = new Bundle();
		args.putString(EXTRA_ALBUM_ID, albumID);
		args.putSerializable(EXTRA_PHOTO_DATA, photo);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			albumID = getArguments().getString(EXTRA_ALBUM_ID);
			photo = (Photo) getArguments().getSerializable(EXTRA_PHOTO_DATA);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo, container, false);
		mImageView = (ImageView) view.findViewById(R.id.imageView);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (!TextUtils.isEmpty(photo.FullName)) {
			ImageLoader.getInstance().displayImage(photo.FullName, mImageView,
					options);
		}
		count = (TextView) getView().findViewById(R.id.comment_count);
		count.setOnClickListener(this);
		getComment();
	}

	private void getComment() {
		final ProgressDialog progress = UIUtils.newProgressDialog(
				getActivity(), "请稍等..");
		JsonHandler handler = new JsonHandler(getActivity()) {
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
				JSONArray array = response.optJSONArray("photoalbumoforums");
				if (array == null)
					return;
				int length = array.length();
				for (int i = 0; i < length; i++) {
					Comment comment = new Comment(array.optJSONObject(i));
					comments.add(comment);
				}
				count.setText(length + "条评论");

			}
		};

		APIService.GetPhotoAlbumForum(albumID, photo.Name, handler);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mImageView != null) {
			mImageView.setImageDrawable(null);
		}
		comments = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_count:// 查看评论
			CommentActivity.startActivity(getActivity(), comments, albumID,
					photo);
			break;
		// 发表评论
		}

	}
}
