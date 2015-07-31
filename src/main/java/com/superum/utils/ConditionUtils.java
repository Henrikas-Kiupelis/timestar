package com.superum.utils;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.TableField;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ConditionUtils {

	public static <R extends Record> Condition betweenDates(TableField<R, Long> field, Date startDate, Date endDate) {
		if (startDate == null)
			return beforeDate(field, endDate);
		
		if (endDate == null)
			return afterDate(field, startDate);
		
		return field.greaterOrEqual(start(startDate))
                .and(field.lessThan(end(endDate)));
	}
	
	public static <R extends Record> Condition beforeDate(TableField<R, Long> field, Date endDate) {
		if (endDate == null)
			return null;
		
		return field.lessThan(end(endDate));
	}
	
	public static <R extends Record> Condition afterDate(TableField<R, Long> field, Date startDate) {
		if (startDate == null)
			return null;
		
		return field.greaterOrEqual(start(startDate));
	}

    private static long start(Date startDate) {
        return startDate.toInstant().truncatedTo(ChronoUnit.DAYS).toEpochMilli();
    }

    private static long end(Date endDate) {
        return endDate.toInstant().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).toEpochMilli();
    }

}
