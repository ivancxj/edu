package com.crazysheep.senate.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.crazysheep.senate.R;

/**
 * User: robin
 * Email: ${EMAIL}
 * Date: 13-4-22
 * Time: AM11:19
 * Package: com.crazysheep.edu.activity
 */
public class RemarkFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_remark, null);
        return view;
    }
}
