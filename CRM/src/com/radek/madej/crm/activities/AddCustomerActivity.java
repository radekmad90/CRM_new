package com.radek.madej.crm.activities;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.R;
import com.radek.madej.crm.database.model.Customer;
import com.radek.madej.crm.utils.ApplicationUtils;
import com.radek.madej.crm.utils.ValidationUtils;

public class AddCustomerActivity extends ActionBarActivity implements OnClickListener {
	
	private Address geocodedAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_customer);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(getTitle());
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_customer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
		case R.id.geocode_button:
			Log.i(CRMApp.TAG, "Geocode button clicked");
			mainParent = (LinearLayout) ((LinearLayout) v.getParent()).getParent();//Gets main LinearLayout
			TextView tv = (TextView) mainParent.findViewById(R.id.new_customer_address);
			Boolean isValid = Boolean.TRUE;// Required for ValidationUtils. Its added because it can be necessary to validate more fields in the future
			if (!ValidationUtils.isEditTextEmpty(this, tv, isValid)) {
				Log.i(CRMApp.TAG, "Break");
				break;
			}
			Intent intent = new Intent(this, GeocodedListActivity.class);
			intent.putExtra(GeocodedListActivity.STRING_TO_GEOCODE, tv.getText().toString());
			startActivityForResult(intent, GeocodedListActivity.PICK_ADDRESS_REQUEST);
			break;
		case R.id.add_customer_button:
			mainParent = (LinearLayout) v.getParent();//Gets main LinearLayout
			//validation
			Boolean valid = Boolean.TRUE;
			TextView companyView = (TextView)  mainParent.findViewById(R.id.new_customer_name);
			ValidationUtils.isEditTextEmpty(this, companyView, valid);
				
			TextView employeeView = (TextView) mainParent.findViewById(R.id.new_customer_employee);
			ValidationUtils.isEditTextEmpty(this, employeeView, valid);
			
			TextView phoneNumberView = (TextView) mainParent.findViewById(R.id.new_customer_phone);
			ValidationUtils.isEditTextEmpty(this, phoneNumberView, valid);
			
			TextView addressView = (TextView) mainParent.findViewById(R.id.new_customer_address);
			ValidationUtils.isEditTextEmpty(this, addressView, valid);
			
			if(geocodedAddress == null) {
				valid = Boolean.FALSE;
				Toast.makeText(this, getResources().getString(R.string.not_geocoded), Toast.LENGTH_LONG).show();
			}
			if (!valid) {
				Log.w(CRMApp.TAG, "Validation unsuccessfull!");
			} else {
				Log.i(CRMApp.TAG, "Validation successfull, trying to add customer to DB");
				Customer newCustomer = new Customer();
				newCustomer.setCompanyName(companyView.getText().toString());
				newCustomer.setEmployee(employeeView.getText().toString());
				newCustomer.setPhone(phoneNumberView.getText().toString());
				newCustomer.setAddress(addressView.getText().toString());//Biorę z widoku bo w onResume() ustawiam mu wartość
				newCustomer.setLattitude(Double.toString(geocodedAddress.getLatitude()));
				newCustomer.setLongitude(Double.toString(geocodedAddress.getLongitude()));
				CRMApp app = (CRMApp) this.getApplication();
				Long newCustomerId = app.getDataManager().saveCustomer(newCustomer);
				if(newCustomerId != 0) {
					Log.i(CRMApp.TAG, "Added client id = " + newCustomerId);
					Toast.makeText(this, getResources().getString(R.string.customer_added_toast), Toast.LENGTH_LONG).show();
					this.finish();
				} else {
					Log.e(CRMApp.TAG,"Nie dodałem klienta!!");
				}
			}
			
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GeocodedListActivity.PICK_ADDRESS_REQUEST) {
			if (resultCode == RESULT_OK) {
				geocodedAddress = (Address) data.getExtras().get("result");
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		if (geocodedAddress != null) {
			Log.i(CRMApp.TAG, "Mam adres w onResume()!");
			View view = getWindow().getDecorView().findViewById(android.R.id.content);
			EditText address = (EditText) view.findViewById(R.id.new_customer_address);
			address.setText(ApplicationUtils.getAddressLinesFromAddress(geocodedAddress));
		}
	}

}
