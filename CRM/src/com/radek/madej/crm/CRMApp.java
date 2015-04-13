package com.radek.madej.crm;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.radek.madej.crm.database.DataManager;
import com.radek.madej.crm.database.DataManagerImpl;

public class CRMApp extends Application {
	public static final String TAG = "madej.radek.crm";
	private DataManager dataManager;

	@Override
	public void onCreate() {
		super.onCreate();
		dataManager = new DataManagerImpl(this);
	}

	public DataManager getDataManager() {
		return dataManager;
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
