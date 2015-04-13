package com.radek.madej.crm.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.R;
import com.radek.madej.crm.adapters.CustomersListAdapter;
import com.radek.madej.crm.database.model.Customer;

public class AllCustomersActivity extends ActionBarActivity implements OnItemClickListener {

	ArrayAdapter<Customer> mAdapter;
	private ListView listView;
	private List<Customer> elementsList;
	public static final String CUSTOMER = "com.radek.madej.customer";
	public static final int PICK_CUSTOMER_REQUEST = 1;
	private Integer requestCode;// Used when you using this activity as
								// startActivityForResult to pick customer from
								// list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		requestCode = intent.getIntExtra("requestCode", 0);

		CRMApp crm = (CRMApp) getApplication();
		elementsList = crm.getDataManager().getAllCustomers();
		setContentView(R.layout.customers_list);
		mAdapter = new CustomersListAdapter(this, elementsList);
		listView = (ListView) findViewById(R.id.customers);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.title_activity_all_customers));
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (requestCode == 0) {
			Intent intent = new Intent(this, CustomerDetailsActivity.class);
			intent.putExtra(CUSTOMER, elementsList.get(position));
			startActivity(intent);
		} else if (requestCode == 1) {
			Intent returnIntent = new Intent();
	        returnIntent.putExtra("result",elementsList.get(position));
	        setResult(RESULT_OK,returnIntent);     
	        finish();
			
		}
	
	}

}
