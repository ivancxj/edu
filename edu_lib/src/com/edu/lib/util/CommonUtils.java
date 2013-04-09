package com.edu.lib.util;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CommonUtils {
	
	public static void hideInputKeyboard(Context context, IBinder iBinder) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(iBinder, 0);
	}

	public static void showInputKeyboard(Context context, View view) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
				.showSoftInput(view, 0);
	}
}
