package com.crazysheep.edu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.edu.lib.bean.Comment;

public class CommentAdapter extends BaseAdapter {
	ArrayList<Comment> comments = null;

	private LayoutInflater mInflater;	
	public CommentAdapter(Context context,ArrayList<Comment> comments){
		mInflater = LayoutInflater.from(context);
		this.comments = comments;
	}
	
	@Override
	public int getCount() {
		if(comments == null) return 0;
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		if(comments == null) return null;
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_comment, null);
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.comment_name);
			holder.time = (TextView)convertView.findViewById(R.id.comment_time);
			holder.content = (TextView)convertView.findViewById(R.id.comment_content);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Comment comment = comments.get(position);
		holder.name.setText(comment.UserName);
		holder.time.setText(comment.intime);
		holder.content.setText(comment.cont);
		
		return convertView;
	}
	
	public final class ViewHolder {
		TextView name;
		TextView time;
		TextView content;
	}


}
