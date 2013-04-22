package com.crazysheep.senate.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazysheep.senate.R;
import com.edu.lib.bean.Named;

public class RemarkAdapter extends BaseAdapter {

	ArrayList<Named> nameds = null;
	Context context;
	private LayoutInflater mInflater;
	public RemarkAdapter(Context context){
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public void add(ArrayList<Named> nameds){
		if(this.nameds == null)
			this.nameds = new ArrayList<Named>();
		
		if(nameds == null) return;
		
		this.nameds.addAll(nameds);
		notifyDataSetInvalidated();
	}
	
	public void clear(){
		if(this.nameds == null)  return ;
		this.nameds.clear();
	}
	
	public void add(Named named){
		if(this.nameds == null)
			this.nameds = new ArrayList<Named>();
		if(named == null) return;
		
		this.nameds.add(named);
		notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		if(nameds == null) return 0;
		return nameds.size();
	}

	@Override
	public Object getItem(int position) {
		if(nameds == null) return null;
		return nameds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.cq_list_item, null);
			holder = new ViewHolder();
			holder.remark_status = (TextView)convertView.findViewById(R.id.remark_status);
			holder.remark_num = (TextView)convertView.findViewById(R.id.remark_num);
			holder.remark_name = (TextView)convertView.findViewById(R.id.remark_name);
			
			holder.remark_time = (TextView)convertView.findViewById(R.id.remark_time);
			holder.remark_show = (ImageView)convertView.findViewById(R.id.remark_show);
			holder.remark_message = (TextView)convertView.findViewById(R.id.remark_message);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Named named = nameds.get(position);
		if(named.IsRecord){
			holder.remark_show.setVisibility(View.GONE);
			holder.remark_status.setVisibility(View.VISIBLE);
			holder.remark_num.setTextColor(context.getResources().getColor(R.color.black));
			holder.remark_name.setTextColor(context.getResources().getColor(R.color.black));
			holder.remark_time.setTextColor(context.getResources().getColor(R.color.black));
		}else{
			holder.remark_show.setVisibility(View.VISIBLE);
			holder.remark_show.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(v.getTag() == null){
						
					}else{
						
					}
					
				}
			});
			holder.remark_status.setVisibility(View.INVISIBLE);
			holder.remark_num.setTextColor(context.getResources().getColor(R.color.red));
			holder.remark_name.setTextColor(context.getResources().getColor(R.color.red));
			holder.remark_time.setTextColor(context.getResources().getColor(R.color.red));
		}
		holder.remark_num.setText(named.SNum);
		holder.remark_name.setText(named.SName);
		holder.remark_time.setText(named.InTime);
		
		return convertView;
	}
	
	public final class ViewHolder {
		TextView remark_status;
		TextView remark_num;
		TextView remark_name;
		TextView remark_time;
		ImageView remark_show;
		TextView remark_message;;
	}
}

