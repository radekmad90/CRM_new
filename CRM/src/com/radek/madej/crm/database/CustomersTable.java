package com.radek.madej.crm.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;


public final class CustomersTable {
	public final static String TABLE_NAME = "customers";
	
	public static class CustomersColumns implements BaseColumns{
		public final static String COMPANY_NAME = "company_name";
		public final static String ADDRESS = "address";
		public final static String EMPLOTYEE = "employee";
		public final static String PHONE = "phone";
		public final static String LATITUDE = "latitude";
		public final static String LONGITUDE = "longitude";
		
	}
	public static final String[] COLUMNSARRAY = new String[] {
		BaseColumns._ID, CustomersColumns.COMPANY_NAME,  CustomersColumns.ADDRESS, CustomersColumns.EMPLOTYEE,
		CustomersColumns.PHONE, CustomersColumns.LATITUDE, CustomersColumns.LONGITUDE
	};
	
	
	
	public static void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABLE_NAME + " ( ");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(CustomersColumns.COMPANY_NAME + " TEXT NOT NULL, ");
		sb.append(CustomersColumns.ADDRESS + " TEXT, ");
		sb.append(CustomersColumns.EMPLOTYEE + " TEXT, ");
		sb.append(CustomersColumns.PHONE + " TEXT, ");
		sb.append(CustomersColumns.LATITUDE + " INTEGER, ");
		sb.append(CustomersColumns.LONGITUDE + " INTEGER ");
		sb.append(");");
		db.execSQL(sb.toString());
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CustomersTable.TABLE_NAME);
		CustomersTable.onCreate(db);
	}
}
