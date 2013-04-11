package com.crazysheep.senate.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazysheep.senate.R;
import com.edu.lib.bean.Named;

public class NamedAdapter extends BaseAdapter {
	ArrayList<Named> nameds = null;

	private LayoutInflater mInflater;

	public NamedAdapter(Context context, ArrayList<Named> nameds) {
		mInflater = LayoutInflater.from(context);
		this.nameds = nameds;
	}

	@Override
	public int getCount() {
		if (nameds == null)
			return 0;
		return nameds.size();
	}

	@Override
	public Object getItem(int position) {
		if (nameds == null)
			return null;
		return nameds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_named, null);
			holder = new ViewHolder();
			holder.t = (ImageView) convertView.findViewById(R.id.img);
			holder.num = (TextView) convertView.findViewById(R.id.named_num);
			holder.name = (TextView) convertView.findViewById(R.id.named_name);
			holder.intime = (TextView) convertView
					.findViewById(R.id.named_intime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Named named = nameds.get(position);
		holder.num.setText(named.SNum);
		holder.name.setText(named.Sname);
		holder.intime.setText(named.InTime);
		if(!named.IsCome)
			holder.t.setImageDrawable(null);

		return convertView;
	}

	public final class ViewHolder {
		ImageView t;
		TextView num;
		TextView name;
		TextView intime;
	}

}
