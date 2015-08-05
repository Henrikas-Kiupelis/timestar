package com.superum.helper.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @deprecated Should use LocalDate from JodaTime to correctly calculate the days
 */
@Deprecated
public class DateUtils {

	public static java.sql.Date sqlNow() {
		return new java.sql.Date(new java.util.Date().getTime());
	}
	
	public static Calendar initialDay(int paymentDay) {
		Calendar calendar = new GregorianCalendar();
		int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		if (currentDay <= paymentDay)
			calendar.add(Calendar.MONTH, -1);
		
		return ensureActualDay(calendar, paymentDay);
	}
	
	public static Calendar finalDay(Calendar calendar, int paymentDay) {
		calendar.add(Calendar.MONTH, 1);
		return ensureActualDay(calendar, paymentDay);
	}
	
	public static Calendar ensureActualDay(Calendar calendar, int paymentDay) {
		int maximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (maximum < paymentDay) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
		} else
            calendar.set(Calendar.DAY_OF_MONTH, paymentDay);
		return calendar;
	}
	
	public static java.sql.Date sqlDate(Calendar calendar) {
		return new java.sql.Date(utilDate(calendar).getTime());
	}
	
	public static java.util.Date utilDate(Calendar calendar) {
		return calendar.getTime();
	}

	// PRIVATE

	private DateUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
	}

}
