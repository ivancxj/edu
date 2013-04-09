package com.crazysheep.senate.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.crazysheep.senate.R;
import com.crazysheep.senate.adapter.CommentAdapter;
import com.edu.lib.bean.Comment;

public class CommentActivity extends Activity implements OnClickListener{

	ListView listview;
	CommentAdapter adapter;
	ArrayList<Comment> comments = new ArrayList<Comment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		listview = (ListView)findViewById(R.id.listview);
		adapter = new CommentAdapter(this, comments);
		listview.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
}
