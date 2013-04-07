package com.crazysheep.senate.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import com.edu.lib.bean.*;

import com.crazysheep.senate.R;
import com.crazysheep.senate.adapter.NamedAdapter;

/**
 * 点名
 * 
 * @author ivan
 * 
 */
public class NamedActivity extends Activity {

	ArrayList<Named> namdeds = new ArrayList<Named>();
	NamedAdapter adapter = null;
	GridView gridview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_named);
		gridview = (GridView) findViewById(R.id.gridview);
		testData();
		adapter = new NamedAdapter(this, namdeds);
		gridview.setAdapter(adapter);
	}

	private void testData() {
		Named named = new Named("小花", "08:30", true);
		namdeds.add(named);
		named = new Named("小花", "08:31", true);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
		named = new Named("小花", "08:32", false);
		namdeds.add(named);
	}

}
