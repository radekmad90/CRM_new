package com.radek.madej.crm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.radek.madej.crm.activities.AddCustomerActivity;
import com.radek.madej.crm.activities.AddMeetingActivity;
import com.radek.madej.crm.activities.AllCustomersActivity;
import com.radek.madej.crm.fragments.BaseDrawerFragment;
import com.radek.madej.crm.fragments.CrmFragment;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	private transient NavigationDrawerFragment mNavigationDrawerFragment;
	private transient CharSequence mTitle;
	private CRMApp CRMApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CRMApp = (CRMApp) getApplication();
		setContentView(R.layout.activity_main);
		CrmFragment fragment = new CrmFragment(100);
		getSupportFragmentManager().beginTransaction()
        .add(R.id.container, fragment).commit();
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Intent intent = null;
		switch (position) {

		case 0:
			intent = new Intent(this, AddCustomerActivity.class);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(this, AllCustomersActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(this, AddMeetingActivity.class);
			startActivity(intent);
			break;
		case 3: 
			intent = new Intent(this, MeetingCalendarActivity.class);
			startActivity(intent);
			
			break;
		case 100:
			BaseDrawerFragment fragment = new CrmFragment(position);
			getSupportFragmentManager().beginTransaction()
	        .add(R.id.container, fragment).commit();
			break;
		default:
			Toast toast = Toast.makeText(getApplicationContext(), "onNavigationDrawerItemSelected Default switch", Toast.LENGTH_LONG);
			toast.show();
			break;
		}

	}

	public void onSectionAttached(int number) {
		if (number == 100) {
			mTitle = getResources().getString(R.string.app_name);
			return;
		}
		mTitle = getResources().getStringArray(R.array.nav_drawer_items)[number];
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Closing Activity")
				.setMessage("Czy na pewno chcesz wyjść?").setPositiveButton("Tak", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}

				}).setNegativeButton("Nie", null).show();

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
		return super.onOptionsItemSelected(item);
	}

}
