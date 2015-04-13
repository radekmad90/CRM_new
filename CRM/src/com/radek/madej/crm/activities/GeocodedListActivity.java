package com.radek.madej.crm.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.R;
import com.radek.madej.crm.adapters.GeocoderListAdapter;
import com.radek.madej.crm.async.GeocoderAsyncTask;
import com.radek.madej.crm.async.GeocoderAsyncTask.GeocoderTaskActions;

public class GeocodedListActivity<GeocodedListAdapter> extends ActionBarActivity implements GeocoderTaskActions, OnItemClickListener {
	public static final String STRING_TO_GEOCODE = "com.radek.madej.crm.stringToGeocode";
	public static final int PICK_ADDRESS_REQUEST = 1;
	public static final int PLEASE_WAIT_DIALOG = 1;
	private ListView listView;
	private List<Address> elementsList;
	ArrayAdapter<Address> mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!((CRMApp) this.getApplication()).isOnline()) {//finish activity if Internet connection is not present
			Toast.makeText(this, "Włącz dostęp do internetu i spróbuj ponownie", 7000).show();
			this.finish();
		}
		elementsList = new ArrayList<Address>();
		setContentView(R.layout.activity_geocoded_list);
		mAdapter = new GeocoderListAdapter(this, elementsList);
		listView = (ListView) findViewById(R.id.geocoded_list_view);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.choose_address));
		
		final GeocoderAsyncTask task = new GeocoderAsyncTask(this, getIntent().getStringExtra(STRING_TO_GEOCODE));
		task.execute();
		this.showDialog(GeocodedListActivity.PLEASE_WAIT_DIALOG, null);
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.geocoded_list, menu);

		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Dialog onCreateDialog(int dialogId) {

		switch (dialogId) {
		case PLEASE_WAIT_DIALOG:
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle("Geokodowanie");
			dialog.setMessage("Proszę czekać....");
			dialog.setCancelable(false);
			return dialog;

		default:
			break;
		}

		return null;
	}

	@Override
	public void taskCompletionResult(List<Address> result) {
		if (result == null || result.size() == 0) {
			Toast.makeText(this, "Nie udało się rozpoznać adresu, spróbuj jeszcze raz", Toast.LENGTH_LONG).show();
			finish();
		}
		elementsList.addAll(result);
		mAdapter.notifyDataSetChanged();
		this.removeDialog(GeocodedListActivity.PLEASE_WAIT_DIALOG);
		Toast.makeText(this, "Geokodowanie ukończone!", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void taskInterrupted() {
		Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent returnIntent = new Intent();
        returnIntent.putExtra("result",elementsList.get(position));
        setResult(RESULT_OK,returnIntent);     
        finish();
	}

}
