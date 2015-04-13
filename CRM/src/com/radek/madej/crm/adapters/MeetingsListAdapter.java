package com.radek.madej.crm.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.R;
import com.radek.madej.crm.database.model.Meeting;

public class MeetingsListAdapter extends ArrayAdapter<Meeting> {
	private List<Meeting> values;
	private Context context;
	private String timeFormat = "HH:mm";
	SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());

	public MeetingsListAdapter(Context context, List<Meeting> list) {
		super(context, R.layout.geocoded_row_layout, list);
		this.values = list;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Meeting row = values.get(position);
		View rowView = inflater.inflate(R.layout.meeting_row_layout, parent, false);

		TextView customerNameTextView = (TextView) rowView.findViewById(R.id.meeting_customer_name);
		customerNameTextView.setText(row.getCustomer().getCompanyName());

		TextView meetingTimeTextView = (TextView) rowView.findViewById(R.id.meeting_time);
		meetingTimeTextView.setText(sdf.format(row.getTime().getTime()));

		TextView customerAddressTextView = (TextView) rowView.findViewById(R.id.meeting_address);
		customerAddressTextView.setText(row.getCustomer().getAddress());

		return rowView;
	}
}
