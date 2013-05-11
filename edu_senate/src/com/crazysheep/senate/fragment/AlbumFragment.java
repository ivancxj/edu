package com.crazysheep.senate.fragment;

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

import com.crazysheep.senate.R;
import com.crazysheep.senate.activity.CreateTopicActivity;
import com.crazysheep.senate.activity.TopicListActivity;
import com.crazysheep.senate.adapter.TopicAdapter;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.bean.Album;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.widget.ScrollGridView;

/**
 * 班级 相册列表
 *
 * @author ivan
 */
public class AlbumFragment extends Fragment implements OnItemClickListener {

    private final static int REQUESTCODE = 101;

    private ArrayList<Album> class_albums = null;

    private ScrollGridView mClassGridView;

    private TopicAdapter class_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

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
        mClassGridView = (ScrollGridView) getView().findViewById(
                R.id.grid_class);

        // 班级
        mClassGridView.setOnItemClickListener(this);

        getClassAlbum();
//		getClassAlbum();
    }

    private void getClassAlbum() {
        JsonHandler handler = new JsonHandler(getActivity()) {
            @Override
            public void onStart() {
                super.onStart();
                getView().findViewById(com.edu.lib.R.id.loading).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                	getView().findViewById(com.edu.lib.R.id.loading).setVisibility(View.GONE);
				} catch (Exception e) {
				}
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                LogUtils.I(LogUtils.ALBUM_USER, response.toString());
                JSONArray array = response.optJSONArray("classalbumlist");
                int length = array.length();
                class_albums = new ArrayList<Album>();
                for (int i = 0; i < length; i++) {
                    Album album = new Album(array.optJSONObject(i));
                    class_albums.add(album);
                }

                Album album = new Album();
                album.isNew = true;
                class_albums.add(album);

                class_adapter = new TopicAdapter(getActivity(), class_albums);
                mClassGridView.setAdapter(class_adapter);
                class_adapter.notifyDataSetInvalidated();

            }
        };
        User user = AppConfig.getAppConfig(getActivity()).getUser();
        if (user != null)
            APIService.GetUserAlbum(user.memberid, user.classID, handler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == getActivity().RESULT_OK) {
            getClassAlbum();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Album album = null;
        String name = "";
        int state = 0;

        album = class_albums.get(position);
//        name = "[班级相册]" + album.sName;
        name = album.sName;
        state = 2;

        if (album.isNew) {
            CreateTopicActivity.startActivity(getActivity(), state, REQUESTCODE);
        } else {
            TopicListActivity.startActivity(getActivity(), name, album, REQUESTCODE);
        }

    }
}
