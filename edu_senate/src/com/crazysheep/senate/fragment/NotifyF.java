package com.crazysheep.senate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazysheep.senate.R;
import com.edu.lib.bean.Announcement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotifyF extends Fragment {

	Announcement announcement;

	public static NotifyF newInstance(Announcement announcement) {
		NotifyF fragment = new NotifyF();
		fragment.announcement = announcement;
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
		TextView title = (TextView)view.findViewById(R.id.notify_title);
		title.setText(announcement.Title);
		TextView text = (TextView)view.findViewById(R.id.notify_txt);
		text.setText(announcement.Contents);
		TextView name = (TextView)view.findViewById(R.id.notify_name);
		name.setText(announcement.Sname);
		TextView sendtime = (TextView)view.findViewById(R.id.notify_sendtime);
		sendtime.setText(announcement.Sendtime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = sdf.parse(announcement.Sendtime);
            sendtime.setText(sdf.format(date));
        } catch (ParseException e) {
        }

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
