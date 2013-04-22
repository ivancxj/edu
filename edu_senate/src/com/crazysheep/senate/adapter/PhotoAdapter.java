package com.crazysheep.senate.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.crazysheep.senate.R;
import com.edu.lib.MyApplication;
import com.edu.lib.bean.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoAdapter extends BaseAdapter {

	ArrayList<Photo> photos;
	private LayoutInflater mInflater;
//	private DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.cacheInMemory().cacheOnDisc().build();

	public PhotoAdapter(Context context, ArrayList<Photo> topics) {
		this.mInflater = LayoutInflater.from(context);
		this.photos = topics;
	}

	@Override
	public int getCount() {
		if (photos == null)
			return 0;
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		if (photos == null)
			return null;
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_photo, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Photo topic = photos.get(position);
		
		if (!TextUtils.isEmpty(topic.FullName)) {
			ImageLoader.getInstance().displayImage(topic.FullName,
					holder.img, MyApplication.gridOptions);
		}

		return convertView;
	}

	public final class ViewHolder {
		ImageView img;
	}

}
