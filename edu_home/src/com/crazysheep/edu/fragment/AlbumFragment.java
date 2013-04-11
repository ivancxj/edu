package com.crazysheep.edu.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.crazysheep.edu.R;
import com.crazysheep.edu.activity.CreateTopicActivity;
import com.crazysheep.edu.activity.TopicListActivity;
import com.crazysheep.edu.adapter.TopicAdapter;
import com.edu.lib.bean.Album;
import com.edu.lib.widget.ScrollGridView;

/**
 * 个人，班级 相册列表
 * 
 * @author ivan
 * 
 */
public class AlbumFragment extends Fragment implements OnItemClickListener {

	private final static int REQUESTCODE = 101;

	private ArrayList<Album> albums = null;

	private ScrollGridView gridView;

	private TopicAdapter adapter;

	boolean person = false;

	public static AlbumFragment newInstance(ArrayList<Album> albums,
			boolean person) {
		AlbumFragment fragment = new AlbumFragment();
		fragment.albums = albums;
		fragment.person = person;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_album, container, false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		gridView = (ScrollGridView) getView().findViewById(R.id.grid_person);
		refresh();
	}

	public void refresh() {
		adapter = new TopicAdapter(getActivity(), albums);
		gridView.setAdapter(adapter);
		// adapter.notifyDataSetInvalidated();
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUESTCODE && resultCode == getActivity().RESULT_OK) {
			// getUserAlbum();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Album album = null;
		String name = "";
		int state = 0;
		album = albums.get(position);
		if (person) {
			name = "[个人相册]" + album.sName;
			state = 1;
		} else {
			name = "[班级相册]" + album.sName;
			state = 2;
		}

		if (album.isNew) {
			CreateTopicActivity
					.startActivity(getActivity(), state, REQUESTCODE);
		} else {
			TopicListActivity.startActivity(getActivity(), name, album);
		}
	}

}
