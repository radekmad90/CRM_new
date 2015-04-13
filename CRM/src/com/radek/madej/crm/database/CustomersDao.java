package com.radek.madej.crm.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.database.CustomersTable.CustomersColumns;
import com.radek.madej.crm.database.model.Customer;

public class CustomersDao implements Dao<Customer> {
	private static final String INSERT = "insert into "
			+ CustomersTable.TABLE_NAME + "(" + CustomersColumns.COMPANY_NAME
			+ ", " + CustomersColumns.ADDRESS + ", " + CustomersColumns.EMPLOTYEE
			+ ", " + CustomersColumns.PHONE + ", " + CustomersColumns.LATITUDE
			+ ", " + CustomersColumns.LONGITUDE + ") values (?, ?, ?, ?, ?, ?)";
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public CustomersDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(CustomersDao.INSERT);

	}

	@Override
	public long save(Customer entity) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, entity.getCompanyName());
		insertStatement.bindString(2, entity.getAddress());
		insertStatement.bindString(3, entity.getEmployee());
		insertStatement.bindString(4, entity.getPhone());
		insertStatement.bindString(5, entity.getLattitude());
		insertStatement.bindString(6, entity.getLongitude());
		return insertStatement.executeInsert();
	}

	@Override
	public int update(Customer entity) {
		final ContentValues values = new ContentValues();
		values.put(CustomersColumns.COMPANY_NAME, entity.getCompanyName());
		values.put(CustomersColumns.ADDRESS, entity.getAddress());
		values.put(CustomersColumns.EMPLOTYEE, entity.getEmployee());
		values.put(CustomersColumns.PHONE, entity.getPhone());
		values.put(CustomersColumns.LATITUDE, entity.getLattitude());
		values.put(CustomersColumns.LONGITUDE, entity.getLongitude());
		return db.update(CustomersTable.TABLE_NAME, values, BaseColumns._ID + "= ?",
				new String[] { String.valueOf(entity.getId()) });
	}

	@Override
	public int delete(Customer entity) {
		if (entity.getId() > 0) {
			return db.delete(CustomersTable.TABLE_NAME, BaseColumns._ID + " =?",
					new String[] { String.valueOf(entity.getId()) });
		} else {
			return 0;
		}
	}

	@Override
	public Customer get(long id) {
		Customer customer = null;
		Cursor c = db.query(CustomersTable.TABLE_NAME,
				CustomersTable.COLUMNSARRAY, BaseColumns._ID + "= ?",
				new String[] { String.valueOf(id) }, null, null, null, "1");
		if (c.moveToFirst()) {
			customer = this.buildCustomerFromCursor(c);
		}
		if (!c.isClosed()) {
			c.close();
		}
		return customer;
	}

	@Override
	public List<Customer> getAll() {
		List<Customer> list = new ArrayList<Customer>();
		Cursor c = db.query(CustomersTable.TABLE_NAME,
				CustomersTable.COLUMNSARRAY, null, null, null, null,null, null);
		if (c.moveToFirst()) {
			do {
				Customer customer = this.buildCustomerFromCursor(c);
				if (customer != null) {
					Log.i(CRMApp.TAG, customer.toString());
					list.add(customer);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	private Customer buildCustomerFromCursor(Cursor c) {
		Customer customer = null;
		if (c != null) {
			customer = new Customer();
			customer.setId(c.getInt(0));
			customer.setCompanyName(c.getString(1));
			customer.setAddress(c.getString(2));
			customer.setEmployee(c.getString(3));
			customer.setPhone(c.getString(4));
			customer.setLattitude(c.getString(5));
			customer.setLongitude(c.getString(6));
			customer.setMeetings(null);
		}
		return customer;
	}

}
