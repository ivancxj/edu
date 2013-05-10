package com.edu.lib.base;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.edu.lib.R;
import com.edu.lib.adapter.MessageAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Message;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;

public class MessageListFragment extends CancelFragment implements OnItemClickListener{
	ListView listview;
	MessageAdapter adapter;
	public final static int REQUEST_CODE = 100;
	int pageindex = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_list, container, false);
		listview = (ListView)view.findViewById(R.id.message_list);
		return view;
	}

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new MessageAdapter(getActivity());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		
		refresh(true);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK)
			refresh(true);
	}
	
	void refresh(final boolean refresh){
		JsonHandler handler = new JsonHandler(getActivity()){
			@Override
			public void onStart() {
				super.onStart();
                getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
            	if(refresh){
            		adapter.clear();
            	}
            	
      
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				 try {
	                	getView().findViewById(R.id.loading).setVisibility(View.GONE);
	                	adapter.notifyDataSetInvalidated();
					} catch (Exception e) {
					}
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
		// TODO
		if(refresh)
			pageindex = 1;
		else
			pageindex++;
		
		APIService.GetPms(user.userid, pageindex, handler);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Message message = (Message)adapter.getItem(position);
		ReplyMessageActivity.startActivity(getActivity(), message,REQUEST_CODE);
	}
}
