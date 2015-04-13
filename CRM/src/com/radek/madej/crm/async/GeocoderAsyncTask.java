package com.radek.madej.crm.async;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.activities.GeocodedListActivity;

public class GeocoderAsyncTask extends AsyncTask<Void, Void, List<Address>> {

	private String query;
	private GeocodedListActivity context;

	public GeocoderAsyncTask(GeocodedListActivity context, String query) {
		super();
		this.query = query;
		this.context = context;
	}

	@Override
	protected List<Address> doInBackground(Void... params) {
		Log.i(CRMApp.TAG, "Geokoduje z zaptytania: " + query);
		Geocoder coder = new Geocoder(context);
		List<Address> outList = null;
		try {
			outList = coder.getFromLocationName(query, 10);
		} catch (IOException e) {
			context.taskInterrupted();
			e.printStackTrace();
		}
		return outList;
	}

	@Override
	protected void onPostExecute(List<Address> result) {
		context.taskCompletionResult(result);

	}

	/**
	 * Interface to interact with calling activity
	 * 
	 * @author radoslaw.madej
	 * 
	 */
	public interface GeocoderTaskActions {
		public void taskCompletionResult(List<Address> result);

		public void taskInterrupted();
	}

}
