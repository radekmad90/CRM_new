package com.radek.madej.crm.database;

import android.os.Environment;

public final class DataConstants {

   private static final String APP_PACKAGE_NAME = "com.radek.madej.crm";

   private static final String EXTERNAL_DATA_DIR_NAME = "crmdata";
   public static final String EXTERNAL_DATA_PATH =
            Environment.getExternalStorageDirectory() + "/" + DataConstants.EXTERNAL_DATA_DIR_NAME;

   public static final String DATABASE_NAME = "crm.db";
   public static final String DATABASE_PATH =
            Environment.getDataDirectory() + "/data/" + DataConstants.APP_PACKAGE_NAME + "/databases/"
                     + DataConstants.DATABASE_NAME;

   private DataConstants() {
   }
}
