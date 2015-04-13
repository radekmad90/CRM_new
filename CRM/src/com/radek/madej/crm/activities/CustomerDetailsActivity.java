package com.radek.madej.crm.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.radek.madej.crm.R;
import com.radek.madej.crm.database.model.Customer;

public class CustomerDetailsActivity extends ActionBarActivity implements OnClickListener {
	private Customer customer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		customer = (Customer) bundle.get(AllCustomersActivity.CUSTOMER);

		setContentView(R.layout.activity_customer_details);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(getTitle());
		actionBar.setDisplayHomeAsUpEnabled(true);

		setUpLabels();

	}

	private void setUpLabels() {
		TextView tv = (TextView) findViewById(R.id.customer_detail_name);
		tv.setText(customer.getCompanyName());

		tv = (TextView) findViewById(R.id.customer_detail_employee);
		tv.setText(customer.getEmployee());

		tv = (TextView) findViewById(R.id.customer_detail_phone);
		tv.setText(customer.getPhone());

		tv = (TextView) findViewById(R.id.customer_detail_address);
		tv.setText(customer.getAddress());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_customer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		LinearLayout mainParent;

		int id = v.getId();
		switch (id) {
		case R.id.meetings_button:
			break;
		case R.id.show_on_map_button:

			break;
		case R.id.call_button:
			String uri = "tel:" + customer.getPhone().trim();
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse(uri));
			startActivity(intent);
			break;
		default:
			break;
		}

	}
}
