package com.crazysheep.senate.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.crazysheep.senate.R;
import com.crazysheep.senate.activity.AlbumActivity;
import com.crazysheep.senate.activity.PhotoActivity;
import com.crazysheep.senate.adapter.PhotoAdapter;
import com.edu.lib.bean.Photo;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * 主题列表
 * @author ivan
 *
 */
public class TopicListFragment extends Fragment implements OnItemClickListener{
	
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private PullToRefreshGridView gridView;
	private PhotoAdapter adapter;
	
	private TextView name;
	
	private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(AlbumActivity.DYNAMICACTION)){  
                String msg = intent.getStringExtra("msg");  
                update(msg);
            }  
        }  
    }; 
    
    private void update(String msg){
    	name.setText(msg);
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_topic_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		name = (TextView)getView().findViewById(R.id.name);
		testData();
		initView();
		
		IntentFilter filter_dynamic = new IntentFilter();  
        filter_dynamic.addAction(AlbumActivity.DYNAMICACTION);  
        getActivity().registerReceiver(dynamicReceiver, filter_dynamic);  
		
	}
	
	private void initView(){
		gridView = (PullToRefreshGridView)getView().findViewById(R.id.grid_view);
		adapter = new PhotoAdapter(getActivity(), photos);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		adapter.notifyDataSetInvalidated();
	}
	
	private void testData(){
		Photo photo = new Photo("http://d01.res.meilishuo.net/pic/r/15/02/e3f9f9d4a85872b11342c4bd419e_445_650.jpeg");
		photos.add(photo);
		photo = new Photo("http://imgtest-lx.meilishuo.net/pic/r/2d/2d/1b2fa1092814bbc621c13785b3f0_319_479.jpg");
		photos.add(photo);
	

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(),PhotoActivity.class);
		startActivity(intent);
		
	}
}
