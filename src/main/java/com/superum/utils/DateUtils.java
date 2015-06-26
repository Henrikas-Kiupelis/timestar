package com.superum.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {

	public static java.sql.Date sqlNow() {
		return new java.sql.Date(new java.util.Date().getTime());
	}
	
	public static Calendar initialDay(byte paymentDay) {
		Calendar calendar = new GregorianCalendar();
		int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		if (currentDay <= paymentDay)
			calendar.add(Calendar.MONTH, -1);
		
		return ensureActualDay(calendar, paymentDay);
	}
	
	public static Calendar finalDay(Calendar calendar, byte paymentDay) {
		calendar.add(Calendar.MONTH, 1);
		return ensureActualDay(calendar, paymentDay);
	}
	
	public static Calendar ensureActualDay(Calendar calendar, int paymentDay) {
		int maximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (maximum < paymentDay)
			paymentDay = maximum;
		
		calendar.set(Calendar.DAY_OF_MONTH, paymentDay);
		return calendar;
	}
	
	public static java.sql.Date sqlDate(Calendar calendar) {
		return new java.sql.Date(utilDate(calendar).getTime());
	}
	
	public static java.util.Date utilDate(Calendar calendar) {
		return calendar.getTime();
	}
	
}
