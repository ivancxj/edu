package com.crazysheep.senate.activity;

import java.util.ArrayList;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazysheep.senate.R;
import com.edu.lib.api.APIService;
import com.edu.lib.api.JsonHandler;
import com.edu.lib.base.ActionBarActivity;
import com.edu.lib.bean.Student;
import com.edu.lib.bean.User;
import com.edu.lib.util.AppConfig;
import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;

/**
 * User: robin Email: ${EMAIL} Date: 13-4-22 Time: PM8:05 Package:
 * com.crazysheep.school.activity
 */
public class ContactActivity extends ActionBarActivity {

    private ListView mListView;
    private StudentAdapter adapter;
    private EditText search;
    private ArrayList<Student> students;

    public static void startActivity(Activity context, int requestCode) {
        Intent data = new Intent(context, ContactActivity.class);
        context.startActivityForResult(data, requestCode);
    }

    private void GetStudentList() {
        final ProgressDialog progress = UIUtils.newProgressDialog(this,
                "请稍候...");
        JsonHandler handler = new JsonHandler(this) {
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
                LogUtils.I(LogUtils.StudentRecord, response.toString());
                JSONArray array = response.optJSONArray("mobileitemstudents");
                if (array == null)
                    return;
                students = new ArrayList<Student>();
                int length = array.length();
                for (int i = 0; i < length; i++) {
                    Student student = new Student(array.optJSONObject(i));
                    students.add(student);
                }
                adapter.add(students);
            }
        };
        User user = AppConfig.getAppConfig(this).getUser();
        APIService.GetStudentList(user.classID, handler);
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
                it.putExtra("data", getSelect());
                setResult(RESULT_OK, it);
                finish();
            }
        });
        search = (EditText) findViewById(R.id.contact_serch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Student> sl = new ArrayList<Student>();
                if (students != null && students.size() > 0) {
                    if (s.toString() == null || s.toString().length() == 0) {
                        sl.addAll(students);
                    } else {
                        for (int i = 0; i < students.size(); i++) {
                            if (students.get(i).SName.startsWith(s.toString())) {
                                students.get(i).isSelect = false;
                                sl.add(students.get(i));
                            }
                        }
                    }
                }
                adapter.clear();
                adapter.add(sl);
                adapter.notifyDataSetChanged();
            }
        });
        GetStudentList();
        mListView = (ListView) findViewById(R.id.list);
        adapter = new StudentAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Student student = (Student) adapter.getItem(position);
                student.isSelect = !student.isSelect;
                adapter.notifyDataSetInvalidated();
            }
        });
    }

    ArrayList<Student> getSelect() {
        if (adapter == null)
            return null;
        ArrayList<Student> tmp = adapter.students;
        if (tmp == null)
            return null;
        ArrayList<Student> students = new ArrayList<Student>();
        for (Student stu : tmp) {
            if (stu.isSelect) {
                students.add(stu);
            }
        }

        return students;
    }

    class StudentAdapter extends BaseAdapter {

        public ArrayList<Student> students;
        Context context;
        private LayoutInflater mInflater;

        public StudentAdapter(Context context) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
        }

        public void add(ArrayList<Student> students) {
            if (this.students == null)
                this.students = new ArrayList<Student>();

            if (students == null)
                return;

            this.students.addAll(students);
            notifyDataSetInvalidated();
        }

        public void clear() {
            if (this.students == null)
                return;
            for (int i = 0; i < students.size(); i++) {
                students.get(i).isSelect = false;
            }
            this.students.clear();
        }

        public void add(Student student) {
            if (this.students == null)
                this.students = new ArrayList<Student>();
            if (student == null)
                return;

            this.students.add(student);
            notifyDataSetInvalidated();
        }

        @Override
        public int getCount() {
            if (students == null)
                return 0;
            return students.size();
        }

        @Override
        public Object getItem(int position) {
            if (students == null)
                return null;
            return students.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.contact_list_item,
                        null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Student student = students.get(position);
            holder.title.setText(student.SName);
            if (student.isSelect) {
                holder.img.setImageResource(R.drawable.ic_selected);
            } else {
                holder.img.setImageResource(R.drawable.ic_unselect);
            }

            return convertView;
        }

        public final class ViewHolder {
            TextView title;
            ImageView img;
        }
    }

}
