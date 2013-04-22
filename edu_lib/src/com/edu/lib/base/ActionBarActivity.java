package com.edu.lib.base;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.edu.lib.R;

public class ActionBarActivity extends FragmentActivity {

    public void setTitle(String title) {
        ((TextView) findViewById(R.id.action_title)).setText(title);
    }

    public void setHomeActionListener(View.OnClickListener listener) {
        findViewById(R.id.action).setOnClickListener(listener);
    }

    public void setRightAction(String title, View.OnClickListener listener) {
        ((Button) findViewById(R.id.action_right)).setText(title);
        findViewById(R.id.action_right).setOnClickListener(listener);
    }

}
