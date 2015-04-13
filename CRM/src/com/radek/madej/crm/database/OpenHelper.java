package com.radek.madej.crm.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

import com.radek.madej.crm.CRMApp;
import com.radek.madej.crm.R;

//
// SQLiteOpenHelper   
//
public class OpenHelper extends SQLiteOpenHelper {

   private static final int DATABASE_VERSION = 3;
   
   private Context context;

   OpenHelper(final Context context) {
      super(context, DataConstants.DATABASE_NAME, null, DATABASE_VERSION);
      this.context = context;
   }

   @Override
   public void onOpen(final SQLiteDatabase db) {
      super.onOpen(db);
      if (!db.isReadOnly()) {
         // versions of SQLite older than 3.6.19 don't support foreign keys
         // and neither do any version compiled with SQLITE_OMIT_FOREIGN_KEY
         // http://www.sqlite.org/foreignkeys.html#fk_enable
         // 
         // make sure foreign key support is turned on if it's there (should be already, just a double-checker)          
         db.execSQL("PRAGMA foreign_keys=ON;");

         // then we check to make sure they're on 
         // (if this returns no data they aren't even available, so we shouldn't even TRY to use them)
         Cursor c = db.rawQuery("PRAGMA foreign_keys", null);
         if (c.moveToFirst()) {
            int result = c.getInt(0);
            Log.i(CRMApp.TAG, "SQLite foreign key support (1 is on, 0 is off): " + result);
         } else {
            // could use this approach in onCreate, and not rely on foreign keys it not available, etc.
            Log.i(CRMApp.TAG, "SQLite foreign key support NOT AVAILABLE");
            // if you had to here you could fall back to triggers
         }
         if (!c.isClosed()) {
            c.close();
         }
      }
   }

   @Override
   public void onCreate(final SQLiteDatabase db) {
      Log.i(CRMApp.TAG, "DataHelper.OpenHelper onCreate creating database " + DataConstants.DATABASE_NAME);

      CustomersTable.onCreate(db);
      MeetingsTable.onCreate(db);
   }

   @Override
   public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
      Log.i(CRMApp.TAG, "SQLiteOpenHelper onUpgrade - oldVersion:" + oldVersion + " newVersion:"
                        + newVersion);

      CustomersTable.onUpgrade(db, oldVersion, newVersion);


      MeetingsTable.onUpgrade(db, oldVersion, newVersion);
   }
}
