package com.radek.madej.crm.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.radek.madej.crm.database.CustomersTable.CustomersColumns;
import com.radek.madej.crm.database.MeetingsTable.MeetingsColumns;
import com.radek.madej.crm.database.model.Customer;
import com.radek.madej.crm.database.model.Meeting;

public class MeetingsDao implements Dao<Meeting> {

	private static final String INSERT = "insert into " + MeetingsTable.TABLE_NAME + "(" + MeetingsColumns.DATE + ", " + MeetingsColumns.DESCRIPTION
			+ ", " + MeetingsColumns.RESULT + ", " + MeetingsColumns.CUSTOMER_ID + ") values (?, ?, ?, ?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public MeetingsDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(MeetingsDao.INSERT);
	}

	@Override
	public long save(Meeting entity) {
		insertStatement.clearBindings();
		insertStatement.bindLong(1, entity.getTime().getTime());
		insertStatement.bindString(2, entity.getDescription());
		if (entity.getResult() == null) {
			entity.setResult("");
		}
		insertStatement.bindString(3, entity.getResult());
		insertStatement.bindLong(4, entity.getCustomer().getId());
		return insertStatement.executeInsert();
	}

	@Override
	public int update(Meeting entity) {
		final ContentValues values = new ContentValues();
		values.put(MeetingsColumns.DATE, entity.getTime().getTime());
		values.put(MeetingsColumns.DESCRIPTION, entity.getDescription());
		values.put(MeetingsColumns.RESULT, entity.getResult());
		values.put(MeetingsColumns.CUSTOMER_ID, entity.getCustomer().getId());
		return db.update(MeetingsTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[] { String.valueOf(entity.getId()) });
	}

	@Override
	public int delete(Meeting entity) {
		if (entity.getId() > 0) {
			return db.delete(MeetingsTable.TABLE_NAME, BaseColumns._ID + " = ?", new String[] { String.valueOf(entity.getId()) });
		} else {
			return 0;
		}
	}

	@Override
	public Meeting get(long id) {
		Meeting meeting = null;
		Cursor c = db.query(MeetingsTable.TABLE_NAME, MeetingsTable.COLUMNSARRAY, BaseColumns._ID + " = ?", new String[] { String.valueOf(id) },
				null, null, null, "1");
		if (c.moveToFirst()) {
			meeting = this.buildMeetingFromCursor(c);
		}
		if (!c.isClosed()) {
			c.close();
		}
		return meeting;
	}

	@Override
	public List<Meeting> getAll() {
		List<Meeting> list = new ArrayList<Meeting>();

		String MY_QUERY = "SELECT * FROM " + MeetingsTable.TABLE_NAME + " m JOIN " + CustomersTable.TABLE_NAME + " c ON m."
				+ MeetingsColumns.CUSTOMER_ID + "=c._id";
		Cursor c = db.rawQuery(MY_QUERY, null);
		// db.query(MeetingsTable.TABLE_NAME,
		// MeetingsTable.COLUMNSARRAY, null, null, null, null,
		// MeetingsColumns.DATE, null);
		if (c.moveToFirst()) {
			do {
				Meeting meeting = this.buildMeetingAndCustomerFromCursor(c);
				if (meeting != null) {
					list.add(meeting);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	// public List<Meeting> find(Date from, Date to, Customer customer ) {
	// List<Meeting> list = new ArrayList<Meeting>)();
	// String sql = "select * from " + MovieTable.TABLE_NAME + " where upper("
	// + MovieColumns.NAME + ") = ? limit 1";
	// Cursor c = db.rawQuery(sql, new String[] { name.toUpperCase() });
	// if (c.moveToFirst()) {
	// movieId = c.getLong(0);
	// }
	// if (!c.isClosed()) {
	// c.close();
	// }

	private Meeting buildMeetingFromCursor(Cursor c) {
		Meeting meeting = null;
		if (c != null) {
			meeting = new Meeting();
			meeting.setId(c.getLong(0));
			meeting.setTime(new Date(c.getLong(1)));
			meeting.setDescription(c.getString(2));
			meeting.setResult(c.getString(3));
		}
		return meeting;
	}

	private Meeting buildMeetingAndCustomerFromCursor(Cursor c) {
		Meeting meeting = null;
		if (c != null) {
			meeting = buildMeetingFromCursor(c);
			Customer cust = new Customer();
			cust.setId(c.getLong(4));
			cust.setCompanyName(c.getString(6));
			cust.setAddress(c.getString(7));
			cust.setEmployee(c.getString(8));
			cust.setPhone(c.getString(9));
			cust.setLattitude(c.getString(10));
			cust.setLongitude(c.getString(11));
			meeting.setCustomer(cust);
		}
		return meeting;
	}

	public List<Meeting> getMeetingsForCustomer(Customer customer) {
		List<Meeting> meetings = new ArrayList<Meeting>();
		Cursor c = db.query(MeetingsTable.TABLE_NAME, MeetingsTable.COLUMNSARRAY, MeetingsColumns.CUSTOMER_ID + " = ?",
				new String[] { String.valueOf(customer.getId()) }, null, null, MeetingsColumns.DATE, null);
		if (c.moveToFirst()) {
			do {
				Meeting meeting = this.buildMeetingFromCursor(c);
				if (meeting != null) {
					meetings.add(meeting);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return meetings;
	}

	public List<Meeting> getMeetingsForDates(Calendar dayStartDate, Calendar dayEndDate) {
		List<Meeting> list = new ArrayList<Meeting>();

		final String query = "SELECT * FROM " + MeetingsTable.TABLE_NAME + " m JOIN " + CustomersTable.TABLE_NAME + " c ON m."
				+ MeetingsColumns.CUSTOMER_ID + "=c._id WHERE m." + MeetingsColumns.DATE + ">=? and m." + MeetingsColumns.DATE + " <= ? order by m."+MeetingsColumns.DATE+ " ";
		final Cursor c = db.rawQuery(query, new String[] {String.valueOf(dayStartDate.getTimeInMillis()), String.valueOf(dayEndDate.getTimeInMillis())});
		if (c.moveToFirst()) {
			do {
				Meeting meeting = this.buildMeetingAndCustomerFromCursor(c);
				if (meeting != null) {
					list.add(meeting);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}
}
