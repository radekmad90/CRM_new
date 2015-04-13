package com.radek.madej.crm.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class MeetingsTable {

	public static final String TABLE_NAME = "meetings";

	public static class MeetingsColumns implements BaseColumns {
		public static final String DATE = "date";
		public static final String DESCRIPTION = "description";
		public static final String RESULT = "result";
		public static final String CUSTOMER_ID = "customer_id";
	}
	public static final String[] COLUMNSARRAY = new String[] {
		BaseColumns._ID, MeetingsColumns.DATE, MeetingsColumns.DESCRIPTION, MeetingsColumns.RESULT,
		MeetingsColumns.CUSTOMER_ID
	};

	public static void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABLE_NAME + " ( ");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(MeetingsColumns.DATE + " INTEGER NOT NULL, ");
		sb.append(MeetingsColumns.DESCRIPTION + " TEXT, ");
		sb.append(MeetingsColumns.RESULT + " TEXT,");
		sb.append(MeetingsColumns.CUSTOMER_ID + " INTEGER NOT NULL, ");
		sb.append("FOREIGN KEY( " + MeetingsColumns.CUSTOMER_ID + ") REFERENCES " + CustomersTable.TABLE_NAME + " (" + BaseColumns._ID + ")");
		sb.append(");");
		db.execSQL(sb.toString());
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MeetingsTable.TABLE_NAME);
		MeetingsTable.onCreate(db);
	}

}
