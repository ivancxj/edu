package com.crazysheep.edu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crazysheep.edu.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoFragment extends Fragment {

	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
	private String mImageUrl;
	private ImageView mImageView;

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory().cacheOnDisc().build();

	public static PhotoFragment newInstance(String imageUrl) {
		final PhotoFragment f = new PhotoFragment();

		final Bundle args = new Bundle();
		args.putString(IMAGE_DATA_EXTRA, imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString(
				IMAGE_DATA_EXTRA) : null;
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

		if (!TextUtils.isEmpty(mImageUrl)) {
			ImageLoader.getInstance().displayImage(mImageUrl, mImageView,
					options);
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
