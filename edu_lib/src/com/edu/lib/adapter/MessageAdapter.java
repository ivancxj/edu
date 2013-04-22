package com.edu.lib.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.lib.bean.Message;

public class MessageAdapter extends BaseAdapter {

	ArrayList<Message> messages;
	Context context;
	private LayoutInflater mInflater;
	public MessageAdapter(Context context){
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public void add(ArrayList<Message> messages){
		if(this.messages == null)
			this.messages = new ArrayList<Message>();
		
		if(messages == null) return;
		
		this.messages.addAll(messages);
		notifyDataSetInvalidated();
	}
	
	public void clear(){
		if(this.messages == null)  return ;
		this.messages.clear();
	}
	
	public void add(Message message){
		if(this.messages == null)
			this.messages = new ArrayList<Message>();
		if(message == null) return;
		
		this.messages.add(message);
		notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		if(messages == null) return 0;
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		if(messages == null) return null;
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(com.edu.lib.R.layout.list_item_message, null);
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(com.edu.lib.R.id.message_name);
			holder.content = (TextView)convertView.findViewById(com.edu.lib.R.id.message_content);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Message message = messages.get(position);
		holder.name.setText(message.SendName);
		holder.content.setText(message.Content);
		return convertView;
	}
	
	public final class ViewHolder {
		TextView name;
		TextView content;
	}
}

