package com.crazysheep.edu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.crazysheep.edu.R;
import com.crazysheep.edu.adapter.CommentAdapter;
import com.edu.lib.bean.Comment;

public class CommentActivity extends Activity implements OnClickListener{

	ListView listview;
	CommentAdapter adapter;
	ArrayList<Comment> comments = new ArrayList<Comment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		testData();
		
		listview = (ListView)findViewById(R.id.listview);
		adapter = new CommentAdapter(this, comments);
		listview.setAdapter(adapter);
	}
	
	private void testData(){
		Comment comment = new Comment("张三","2013-03-05 12:12:12","en,好图");
		comments.add(comment);
		comment = new Comment("王五","2012-03-14 05:12:22","风景很好");
		comments.add(comment);
		comment = new Comment("小花","2013-06-18 08:02:14","风景不错");
		comments.add(comment);
		comment = new Comment("李四","2013-01-16 10:12:13","好好学习");
		comments.add(comment);
		comment = new Comment("阿花","2014-02-13 11:15:12","天天向上");
		comments.add(comment);
		comment = new Comment("花花","2013-04-15 13:17:12","老师好");
		comments.add(comment);
		
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
