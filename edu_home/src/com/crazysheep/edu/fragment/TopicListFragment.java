package com.crazysheep.edu.fragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.crazysheep.edu.R;
import com.crazysheep.edu.activity.AlbumActivity;
import com.crazysheep.edu.activity.PhotoActivity;
import com.crazysheep.edu.activity.TabHostActivity;
import com.crazysheep.edu.adapter.PhotoAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Album;
import com.edu.lib.bean.Photo;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * 主题列表 相册照片列表
 * 
 * @author ivan
 * 
 */
public class TopicListFragment extends Fragment implements OnItemClickListener {

	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private PullToRefreshGridView gridView;
	private PhotoAdapter adapter;

	private TextView name;
	public final static String EXTRA_FILENAME = "extra_filename";
	public final static String EXTRA_MSG = "extra_msg";
	public final static String EXTRA_ALBUM = "extra_album";
	private Album album;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 动态
			if (intent.getAction().equals(AlbumActivity.DYNAMICACTION)) {
				String msg = intent.getStringExtra(EXTRA_MSG);
				album = (Album) intent.getSerializableExtra(EXTRA_ALBUM);
				update(msg);
				updateAlbumPhotoLis();
			
			}else if(intent.getAction().equals(TabHostActivity.UPLOADACTION)){// upload
				String resume_file = intent.getStringExtra(EXTRA_FILENAME);
				System.err.println("resume_file = "+resume_file);
				UploadAlbumPhotos(resume_file);
			}
		}
	};

	private void UploadAlbumPhotos(String resume_file) {
		final ProgressDialog progress = UIUtils.newProgressDialog(getActivity(), "请稍候...");
		JsonHandler handler = new JsonHandler(getActivity()) {
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
				LogUtils.I(LogUtils.UPLOAD_PHOTO, response.toString());
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		
//		ContentResolver resolver = getActivity().getContentResolver(); 
//		try {
//			InputStream  inStream=resolver.openInputStream(Uri.parse(resume_file));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} 
		APIService.UploadAlbumPhotos(album.albumID, album.gid, user.classID,
				user.memberid, resume_file, handler);
		
	}

	private void update(String msg) {
		name.setText(msg);
	}

	private void updateAlbumPhotoLis() {
		JsonHandler handler = new JsonHandler(getActivity()) {
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
				LogUtils.I(LogUtils.PhotoList, response.toString());
			}
		};
		User user = AppConfig.getAppConfig(getActivity()).getUser();
		APIService.GetAlbumPhotoList(album.albumID, album.gid, user.classID,
				user.memberid, handler);
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
		name = (TextView) getView().findViewById(R.id.name);
		testData();
		initView();

		IntentFilter filter_dynamic = new IntentFilter();
		filter_dynamic.addAction(AlbumActivity.DYNAMICACTION);
		filter_dynamic.addAction(TabHostActivity.UPLOADACTION);
		getActivity().registerReceiver(receiver, filter_dynamic);

	}

	private void initView() {
		gridView = (PullToRefreshGridView) getView().findViewById(
				R.id.grid_view);
		adapter = new PhotoAdapter(getActivity(), photos);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		adapter.notifyDataSetInvalidated();
	}

	private void testData() {
		Photo photo = new Photo(
				"http://d01.res.meilishuo.net/pic/r/15/02/e3f9f9d4a85872b11342c4bd419e_445_650.jpeg");
		photos.add(photo);
		photo = new Photo(
				"http://imgtest-lx.meilishuo.net/pic/r/2d/2d/1b2fa1092814bbc621c13785b3f0_319_479.jpg");
		photos.add(photo);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), PhotoActivity.class);
		startActivity(intent);
//		UploadAlbumPhotos("");
		

	}
}
