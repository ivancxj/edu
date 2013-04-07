package com.crazysheep.edu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazysheep.edu.R;

public class NotifyFragment extends Fragment {

	private String mContent = "???";

	public static NotifyFragment newInstance(String content) {
		NotifyFragment fragment = new NotifyFragment();
		fragment.mContent = content;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notify, container, false);
		TextView text = (TextView)view.findViewById(R.id.notify_txt);
		text.setText(Html.fromHtml(mContent));

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
