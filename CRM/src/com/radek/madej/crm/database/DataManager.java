package com.radek.madej.crm.database;

import java.util.Calendar;
import java.util.List;

import android.database.Cursor;

import com.radek.madej.crm.database.model.Customer;
import com.radek.madej.crm.database.model.CustomersSearchBean;
import com.radek.madej.crm.database.model.Meeting;
import com.radek.madej.crm.database.model.MeetingsSearchBean;

public interface DataManager {
	Customer getCustomer(long customerId);

	List<Customer> getAllCustomers();

	List<Customer> findCustomers(CustomersSearchBean csb);

	long saveCustomer(Customer customer);

	boolean updateCustomer(Customer customer);

	boolean deleteCustomer(long customerId);

	Cursor getCustomersCursor();

	Meeting getMeeting(long meetingId);

	List<Meeting> getAllMeetings();

	List<Meeting> findMeetings(MeetingsSearchBean msb);

	long saveMeeting(Meeting meeting);

	boolean updateMeeting(Meeting meeting);

	boolean deleteMeeting(long meetingId);

	List<Meeting> getMeetingsForDates(Calendar dayStartDate, Calendar dayEndDate);

}
