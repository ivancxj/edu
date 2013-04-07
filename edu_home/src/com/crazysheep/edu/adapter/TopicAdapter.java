package com.crazysheep.edu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.edu.lib.bean.Album;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TopicAdapter extends BaseAdapter {

//	ArrayList<Topic> topics;
	ArrayList<Album> topics;
	private LayoutInflater mInflater;
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory().cacheOnDisc().build();

	public TopicAdapter(Context context, ArrayList<Album> topics) {
		this.mInflater = LayoutInflater.from(context);
		this.topics = topics;
	}

	@Override
	public int getCount() {
		if (topics == null)
			return 0;
		return topics.size();
	}

	@Override
	public Object getItem(int position) {
		if (topics == null)
			return null;
		return topics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_album, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			holder.newTopic = (TextView) convertView.findViewById(R.id.new_topic);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Album topic = topics.get(position);
		if (topic.isNew) {
			holder.img.setVisibility(View.GONE);
			holder.name.setVisibility(View.GONE);
			holder.count.setVisibility(View.GONE);
			holder.newTopic.setVisibility(View.VISIBLE);
		} else {
			if (!TextUtils.isEmpty(topic.cover)) {
				ImageLoader.getInstance().displayImage(topic.cover,
						holder.img, options);
			}

			holder.name.setText(topic.sName);
			holder.count.setText(topic.photocount + "");

		}

		return convertView;
	}

	public final class ViewHolder {
		ImageView img;
		TextView name;
		TextView count;
		TextView newTopic;
	}

}
