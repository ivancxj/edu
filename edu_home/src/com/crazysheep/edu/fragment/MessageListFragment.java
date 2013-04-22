package com.crazysheep.edu.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.crazysheep.edu.R;
import com.edu.lib.adapter.MessageAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Message;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;

public class MessageListFragment extends Fragment implements OnItemClickListener{
	ListView listview;
	MessageAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_list, container, false);
		listview = (ListView)view.findViewById(R.id.message_list);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new MessageAdapter(getActivity());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		JsonHandler handler = new JsonHandler(getActivity()){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.MESSAGE, response.toString());
				JSONArray array = response.optJSONArray("pmss");
				if(array == null)  return;
				ArrayList<Message> messages = new ArrayList<Message>();
				int length = array.length();
				for(int i = 0;i<length;i++){
					Message message = new Message(array.optJSONObject(i));
					messages.add(message);
				}
				adapter.add(messages);
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetPms(user.userid, 1, handler);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
