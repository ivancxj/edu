package com.crazysheep.school.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.crazysheep.school.R;
import com.edu.lib.base.ActionBarActivity;

/**
 * User: robin
 * Email: ${EMAIL}
 * Date: 13-4-22
 * Time: PM8:05
 * Package: com.crazysheep.school.activity
 */
public class ContactActivity extends ActionBarActivity {

    private ListView mListView;
    private MyAdapter myAdapter;
    private int lastIndex;

    public static void startActivity(Activity context, String... datas) {
        Intent data = new Intent(context, ContactActivity.class);
        if (datas != null) {
            data.putExtra("datas", datas);
        }
        context.startActivity(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        setTitle("联系人");
        setHomeActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRightAction("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra("data", lastIndex);
                setResult(RESULT_OK, it);
                finish();
            }
        });
        mListView = (ListView) findViewById(R.id.list);
        myAdapter = new MyAdapter(this, R.layout.contact_list_item, R.id.title);
        Intent data = getIntent();
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String[] array = bundle.getStringArray("datas");
                for (int i = 0; i < array.length; i++) {
                    myAdapter.add(array[i]);
                }

            }
        }
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View childAt = parent.getChildAt(i);
                    childAt.findViewById(R.id.img).setBackgroundResource(R.drawable.ic_unselect);
                }
                view.findViewById(R.id.img).setBackgroundResource(R.drawable.ic_selected);
                lastIndex = position;
            }
        });
    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);


            return super.getView(position, convertView, parent);
        }


    }


}
