package com.edu.lib.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class UIUtils {

	public static void replaceFragment(FragmentManager fragmentManager,
			Fragment newFragment, int reId, boolean addToBackStack) {

		FragmentTransaction transaction = fragmentManager.beginTransaction();

		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		if (addToBackStack) {
			transaction.addToBackStack(null);
		} else {
			transaction.disallowAddToBackStack();
			Fragment sourceFragment = fragmentManager.findFragmentById(reId);
			Log.d("debug", "remove Fragment => " + sourceFragment + "");
			if (sourceFragment != null)
				transaction.remove(sourceFragment);
		}
		transaction.replace(reId, newFragment);
		// Commit the transaction
		transaction.commit();
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showErrToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static ProgressDialog newProgressDialog(final Activity activity,
			String msg) {
		ProgressDialog progressDialog = new ProgressDialog(activity);
		// progressDialog.setTitle(progressDialogTitleId);
		progressDialog.setMessage(msg);
		progressDialog.setIndeterminate(true);
		progressDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				activity.onKeyDown(keyCode, event);
				return false;
			}
		});
		return progressDialog;
	}
	
	public static void safeShow(ProgressDialog progress) {
		if (progress != null) {
			try {
				progress.show();
			} catch (Exception e) {
			}
		}
	}

	public static void safeDismiss(ProgressDialog progress) {
		if (progress != null) {
			try {
				if(progress.isShowing())
					progress.dismiss();
			} catch (Exception e) {
			}
		}
	}
}
