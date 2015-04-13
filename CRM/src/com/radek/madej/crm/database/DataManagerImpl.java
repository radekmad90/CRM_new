package com.radek.madej.crm.database;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.database.CustomersTable.CustomersColumns;
import com.radek.madej.crm.database.model.Customer;
import com.radek.madej.crm.database.model.CustomersSearchBean;
import com.radek.madej.crm.database.model.Meeting;
import com.radek.madej.crm.database.model.MeetingsSearchBean;

public class DataManagerImpl implements DataManager {
	private Context context;

	private SQLiteDatabase db;

	private CustomersDao customersDao;
	private MeetingsDao meetingsDao;

	public DataManagerImpl(Context context) {
		Log.i(CRMApp.TAG, "DataManager init");
		this.context = context;
		SQLiteOpenHelper openHelper = new OpenHelper(this.context);
		db = openHelper.getWritableDatabase();
		// openHelper.onUpgrade(db, 1, 2);

		this.customersDao = new CustomersDao(db);
		this.meetingsDao = new MeetingsDao(db);
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	private void openDb() {
		if (!db.isOpen()) {
			db = SQLiteDatabase.openDatabase(DataConstants.DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
			// since we pass db into DAO, have to recreate DAO if db is
			// re-opened
			customersDao = new CustomersDao(db);
			meetingsDao = new MeetingsDao(db);
		}
	}

	private void closeDb() {
		if (db.isOpen()) {
			db.close();
		}
	}

	private void resetDb() {
		Log.i(CRMApp.TAG, "Resetting database connection (close and re-open).");
		closeDb();
		SystemClock.sleep(500);
		openDb();
	}

	@Override
	public Customer getCustomer(long customerId) {
		Customer customer = customersDao.get(customerId);
		if (customer != null) {
			customer.setMeetings(meetingsDao.getMeetingsForCustomer(customer));
		}
		return customer;
	}

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> list = customersDao.getAll();
		if (list != null && list.size() > 0) {
			for (Customer customer : list) {
				customer.setMeetings(meetingsDao.getMeetingsForCustomer(customer));
			}
		}
		return list;
	}

	@Override
	public List<Customer> findCustomers(CustomersSearchBean csb) {
		throw new RuntimeException();
	}

	@Override
	public long saveCustomer(Customer customer) {
		return customersDao.save(customer);
	}

	@Override
	public boolean updateCustomer(Customer customer) {
		int result = customersDao.update(customer);
		return result > 0 ? true : false;
	}

	@Override
	public boolean deleteCustomer(long customerId) {
		boolean result = false;
		try {
			db.beginTransaction();
			Customer customer = getCustomer(customerId); // It is important to
															// use getCustomer
															// instead of
															// customersDao
															// because we need
															// meetings
			if (customer != null) {
				for (Meeting meeting : customer.getMeetings()) {
					meetingsDao.delete(meeting);
				}
				customersDao.delete(customer);
			}
			db.setTransactionSuccessful();
			result = true;
		} catch (SQLException e) {
			Log.e(CRMApp.TAG, "Error deleting customer (transaction rolled back)", e);
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * Thats for a list adapter
	 */
	@Override
	public Cursor getCustomersCursor() {

		return db.rawQuery("select " + CustomersColumns._ID + ", " + CustomersColumns.COMPANY_NAME + ", " + CustomersColumns.ADDRESS + ", "
				+ CustomersColumns.EMPLOTYEE + " from " + CustomersTable.TABLE_NAME, null);
	}

	@Override
	public Meeting getMeeting(long meetingId) {
		return meetingsDao.get(meetingId);
	}

	@Override
	public List<Meeting> getAllMeetings() {
		List<Meeting> meetings = meetingsDao.getAll();
		for (Meeting meeting : meetings) {
			meeting.setCustomer(customersDao.get(meeting.getCustomer().getId()));
		}
		return meetings;
	}

	@Override
	public List<Meeting> findMeetings(MeetingsSearchBean msb) {
		throw new RuntimeException();
	}

	@Override
	public long saveMeeting(Meeting meeting) {
		return meetingsDao.save(meeting);
	}

	@Override
	public boolean updateMeeting(Meeting meeting) {
		return meetingsDao.update(meeting) > 0 ? true : false;
	}

	@Override
	public boolean deleteMeeting(long meetingId) {
		Meeting meeting = getMeeting(meetingId);
		return meetingsDao.delete(meeting) > 0 ? true : false;
	}

	@Override
	public List<Meeting> getMeetingsForDates(Calendar dayStartDate, Calendar dayEndDate) {
		return meetingsDao.getMeetingsForDates(dayStartDate, dayEndDate);
	}

}
