package com.radek.madej.crm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;

import com.radek.madej.crm.adapters.CustomersListAdapter;
import com.radek.madej.crm.adapters.MeetingsListAdapter;
import com.radek.madej.crm.database.model.Meeting;
import com.radek.madej.crm.utils.ApplicationUtils;

public class MeetingCalendarActivity extends ActionBarActivity implements OnClickListener, OnDateChangeListener, OnItemClickListener {
	private CalendarView calendarView;
	private ListView listView;
	private List<Meeting> meetingsList;
	private Calendar dayStartDate;
	private Calendar dayEndDate;
	private CRMApp crm;
	
	MeetingsListAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetings_calendar);
		calendarView = (CalendarView) findViewById(R.id.meeting_calendar_view);
		calendarView.setDate(Calendar.getInstance().getTimeInMillis());
		calendarView.setOnDateChangeListener(this);
		
		dayStartDate = Calendar.getInstance();
		dayEndDate = Calendar.getInstance();
		ApplicationUtils.roundDates(dayStartDate, dayEndDate);
		
		
		
		listView = (ListView) findViewById(R.id.meeting_calendar_listView);
		
		crm = (CRMApp) getApplication();
		meetingsList = crm.getDataManager().getMeetingsForDates(dayStartDate, dayEndDate);
		
		mAdapter = new MeetingsListAdapter(this, meetingsList);
		listView = (ListView) findViewById(R.id.meeting_calendar_listView);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(getTitle());
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.meeting_calendar, menu);
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
	public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
		Log.i(CRMApp.TAG, "wybrana data!" + calendarView.getDate());
		
		dayStartDate.set(Calendar.YEAR, year);
		dayStartDate.set(Calendar.MONTH, month);
		dayStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		dayEndDate.set(Calendar.YEAR, year);
		dayEndDate.set(Calendar.MONTH, month);
		dayEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		ApplicationUtils.roundDates(dayStartDate, dayEndDate);
		mAdapter.clear();
		meetingsList.clear();
		meetingsList.addAll(crm.getDataManager().getMeetingsForDates(dayStartDate, dayEndDate));
		mAdapter.notifyDataSetChanged();
		
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.i(CRMApp.TAG, "kliknieto element listy");
		
	}
}
