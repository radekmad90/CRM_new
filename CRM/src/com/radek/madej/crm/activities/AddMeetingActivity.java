package com.radek.madej.crm.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.R;
import com.radek.madej.crm.database.model.Customer;
import com.radek.madej.crm.database.model.Meeting;
import com.radek.madej.crm.utils.ValidationUtils;

public class AddMeetingActivity extends ActionBarActivity implements OnClickListener {

	private Customer customer;
	private TextView timeTextView;
	private TextView dateTextView;
	private TextView descriptionTextView;
	private Calendar meetingDate;
	private Meeting meeting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_meeting);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(getTitle());
		actionBar.setDisplayHomeAsUpEnabled(true);
		timeTextView = (TextView) findViewById(R.id.meeting_time_text_view);
		dateTextView = (TextView) findViewById(R.id.meeting_date_text_view);
		descriptionTextView = (TextView) findViewById(R.id.meeting_description);
		this.meetingDate = Calendar.getInstance();
		updateTimeLabels();

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
		int id = v.getId();
		switch (id) {
		case R.id.choose_customer:
			Log.i(CRMApp.TAG, "Choose customer button clicked");
			Intent intent = new Intent(this, AllCustomersActivity.class);
			intent.putExtra("requestCode", AllCustomersActivity.PICK_CUSTOMER_REQUEST);
			startActivityForResult(intent, AllCustomersActivity.PICK_CUSTOMER_REQUEST);
			break;
		case R.id.meeting_time_text_view:
			DialogFragment timeFragment = new TimePickerFragment(meetingDate);
			timeFragment.show(getSupportFragmentManager(), "timePicker");
			break;
		case R.id.meeting_date_text_view:
			DialogFragment dateFragment = new DatePickerFragment(meetingDate);
			dateFragment.show(getSupportFragmentManager(), "datePicker");
			break;
		case R.id.add_meeting_button:
			// validation
			Boolean valid = Boolean.TRUE;
			ValidationUtils.isEditTextEmpty(this, descriptionTextView, valid);

			if (customer == null) {
				valid = Boolean.FALSE;
				Toast.makeText(this, getResources().getString(R.string.customer_isnt_selected), Toast.LENGTH_LONG).show();
			}
			if (!valid) {
				Log.w(CRMApp.TAG, "Validation unsuccessfull!");
			} else {
				Log.i(CRMApp.TAG, "Validation successfull, trying to add meeting to DB id= " + customer.getId());

				meeting = new Meeting();
				meeting.setCustomer(customer);
				meeting.setDescription(descriptionTextView.getText().toString());
				meeting.setTime(meetingDate.getTime());

				CRMApp app = (CRMApp) this.getApplication();
				Long meetingId = app.getDataManager().saveMeeting(meeting);
				if (meetingId != 0) {
					Log.i(CRMApp.TAG, "Added meeting id = " + meetingId);
					Toast.makeText(this, getResources().getString(R.string.meeting_added_toast), Toast.LENGTH_LONG).show();
					this.finish();
				} else {
					Log.e(CRMApp.TAG, "Nie dodałem spotkania!!");
				}
			}

			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AllCustomersActivity.PICK_CUSTOMER_REQUEST) {
			if (resultCode == RESULT_OK) {
				customer = (Customer) data.getExtras().get("result");
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		if (customer != null) {
			Log.i(CRMApp.TAG, "Mam klienta w onResume()!");
			View view = getWindow().getDecorView().findViewById(android.R.id.content);
			TextView customerView = (TextView) view.findViewById(R.id.meeting_customer_name);
			customerView.setText(customer.getCompanyName());
		}
	}

	public void updateTimeLabels() {
		String timeFormat = "HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
		timeTextView.setText(sdf.format(meetingDate.getTime()));

		String datePattern = "EEEE. dd MMMM yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
		dateTextView.setText(dateFormat.format(meetingDate.getTime()));

	}

	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		Calendar time;

		public TimePickerFragment(Calendar time) {
			this.time = time;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int hour = time.get(Calendar.HOUR_OF_DAY);
			int minute = time.get(Calendar.MINUTE);

			return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			time.set(Calendar.HOUR_OF_DAY, hourOfDay);
			time.set(Calendar.MINUTE, minute);
			AddMeetingActivity activity = (AddMeetingActivity) getActivity();
			activity.updateTimeLabels();
		}
	}

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		private Calendar time;

		public DatePickerFragment(Calendar time) {
			this.time = time;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int year = time.get(Calendar.YEAR);
			int month = time.get(Calendar.MONTH);
			int day = time.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			time.set(Calendar.YEAR, year);
			time.set(Calendar.MONTH, month);
			time.set(Calendar.DAY_OF_MONTH, day);
			AddMeetingActivity activity = (AddMeetingActivity) getActivity();
			activity.updateTimeLabels();
		}
	}
}
