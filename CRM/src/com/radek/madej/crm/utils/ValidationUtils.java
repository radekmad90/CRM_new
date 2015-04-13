package com.radek.madej.crm.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.R;
/**
 * 
 * 
 * @author radoslaw.madej
 *
 */
public final class ValidationUtils {

	/**
	 * Method validates if sent EditText is empty. 
	 * 
	 * @author radoslaw.madej
	 *
	 */

	public static boolean isEditTextEmpty(final Activity activity, final TextView textView, Boolean valid) {
		if (textView.getText().toString().equals("")) {
			Log.d(CRMApp.TAG, "EditText is empty");
			textView.setError(activity.getResources().getString(R.string.no_data));
			valid = Boolean.FALSE;
		}
		return valid;
	}
}
