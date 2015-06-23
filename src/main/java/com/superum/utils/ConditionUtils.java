package com.superum.utils;

import java.sql.Date;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.TableField;

public class ConditionUtils {

	public static <R extends Record> Condition betweenDates(TableField<R, Date> field, Date start, Date end) {
		if (start == null)
			return beforeDate(field, end);
		
		if (end == null)
			return afterDate(field, start);
		
		return field.ge(start).and(field.le(end));
	}
	
	public static <R extends Record> Condition beforeDate(TableField<R, Date> field, Date end) {
		if (end == null)
			return null;
		
		return field.le(end);
	}
	
	public static <R extends Record> Condition afterDate(TableField<R, Date> field, Date start) {
		if (start == null)
			return null;
		
		return field.ge(start);
	}

}
