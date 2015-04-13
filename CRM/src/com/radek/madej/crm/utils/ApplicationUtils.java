package com.radek.madej.crm.utils;

import java.util.Calendar;

import android.location.Address;
import android.util.Log;

import com.radek.madej.crm.CRMApp;

public final class ApplicationUtils {
	
	public static String getAddressLinesFromAddress(Address adr) {
		String address = "";
		
		for(int i = 0; i < adr.getMaxAddressLineIndex(); i++) {
			Log.i(CRMApp.TAG, adr.getAddressLine(i));
			address += adr.getAddressLine(i) + " ";
        }
		return address;
	}
	public static void roundDates (Calendar start, Calendar end) {
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		
		end.set(Calendar.HOUR_OF_DAY, 24);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
	}
}
