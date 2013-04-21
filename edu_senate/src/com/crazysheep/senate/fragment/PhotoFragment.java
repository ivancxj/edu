package com.crazysheep.senate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crazysheep.senate.R;
import com.edu.lib.MyApplication;
import com.edu.lib.bean.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

// 相册详情
public class PhotoFragment extends Fragment{

	private final static String EXTRA_ALBUM_ID = "extra_album_id";
	private static final String EXTRA_PHOTO_DATA = "extra_photo_data";
	private String albumID;
	private Photo photo;
	private ImageView mImageView;

//	private DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.cacheInMemory().cacheOnDisc().build();

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
					MyApplication.options);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mImageView != null) {
			mImageView.setImageDrawable(null);
		}
	}
}
