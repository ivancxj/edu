package com.crazysheep.edu.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.crazysheep.edu.R;
import com.crazysheep.edu.activity.AlbumActivity;
import com.crazysheep.edu.activity.CreateTopicActivity;
import com.crazysheep.edu.adapter.TopicAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Album;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
import com.edu.lib.widget.ScrollGridView;

/**
 * 相册
 * 
 * @author ivan
 * 
 */
public class AlbumFragment extends Fragment implements OnItemClickListener {

	private ArrayList<Album> person_topics = new ArrayList<Album>();
	private ArrayList<Album> class_topics = new ArrayList<Album>();

	private ScrollGridView mPersonGridView;
	private ScrollGridView mClassGridView;

	private TopicAdapter person_adapter;
	private TopicAdapter class_adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_album, container, false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mPersonGridView = (ScrollGridView) getView().findViewById(
				R.id.grid_person);
		mClassGridView = (ScrollGridView) getView().findViewById(
				R.id.grid_class);
		// 个人
		person_adapter = new TopicAdapter(getActivity(), person_topics);
		mPersonGridView.setAdapter(person_adapter);
		mPersonGridView.setOnItemClickListener(this);

		// 班级
		class_adapter = new TopicAdapter(getActivity(), class_topics);
		mClassGridView.setAdapter(class_adapter);
		mClassGridView.setOnItemClickListener(this);
		
		getData();
	}
	
	private void getData(){
		final ProgressDialog progress = UIUtils.newProgressDialog(getActivity(), "请稍候...");
		JsonHandler handler = new JsonHandler(getActivity()){
			@Override
			public void onStart() {
				super.onStart();
				UIUtils.safeShow(progress);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				UIUtils.safeDismiss(progress);
			}
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.ALBUM_CLASS, response.toString());
				JSONArray array = response.optJSONArray("classalbumlist");
				int length = array.length();
				for(int i=0;i<length;i++){
					Album album = new Album(array.optJSONObject(i));
					class_topics.add(album);
				}
				
				class_adapter.notifyDataSetInvalidated();
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetClassAlbum(user.classID, handler);
		getUserAlbum();
	}
	
	private void  getUserAlbum(){
		final ProgressDialog progress = UIUtils.newProgressDialog(getActivity(), "请稍候...");
		JsonHandler handler = new JsonHandler(getActivity()){
			@Override
			public void onStart() {
				super.onStart();
				UIUtils.safeShow(progress);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				UIUtils.safeDismiss(progress);
			}
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				LogUtils.I(LogUtils.ALBUM_USER, response.toString());
				JSONArray array = response.optJSONArray("classalbumlist");
				int length = array.length();
				for(int i=0;i<length;i++){
					Album album = new Album(array.optJSONObject(i));
					person_topics.add(album);
				}
				
				Album album = new Album();
				
				person_adapter.notifyDataSetInvalidated();
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetUserAlbum(user.memberid, handler);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Album topic = null;
		String name = "";
		int state = 0;
		if (mPersonGridView == view.getParent()) {
			topic = person_topics.get(position);
			name = "[个人相册]" + topic.sName;
			state = 1;
		} else if (mClassGridView == view.getParent()) {
			topic = class_topics.get(position);
			name = "[班级相册]" + topic.sName;
			state = 2;
		}
		
		if (topic.isNew) {
			CreateTopicActivity.startActivity(getActivity(), state);
		} else {
			AlbumActivity activity = (AlbumActivity) getActivity();
			activity.pager.setCurrentItem(1);
			Intent intent = new Intent();
			intent.setAction(AlbumActivity.DYNAMICACTION);
			intent.putExtra("msg", name);
			activity.sendBroadcast(intent);
		}

	}
}
