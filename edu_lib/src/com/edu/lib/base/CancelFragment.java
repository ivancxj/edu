package com.edu.lib.base;

import android.support.v4.app.Fragment;

import com.edu.lib.api.APIService;

public class CancelFragment extends Fragment {
    @Override
    public void onStop() {
    	super.onStop();
    	APIService.cancelAllRequest(getActivity());
    }

}
