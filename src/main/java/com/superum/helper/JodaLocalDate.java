package com.superum.helper;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

import java.text.ParseException;

import static com.superum.utils.TimeUtils.getDefaultChronology;
import static com.superum.utils.TimeUtils.getDefaultTimeZone;

/**
 * <pre>
 * Customized LocalDate conversion class
 *
 * USE THIS CLASS EVERY TIME WHEN DEALING WITH RAW DATES
 * i.e. "2015-08-03"
 * TO CONVERT IT TO APPROPRIATE FORMAT
 *
 * The resulting Instants/Dates/etc will assume UTC, 00:00:00.000 time when converting
 * the only exception is java.sql.Date which will use String valueOf LocalDate,
 * thus preserving the date portion regardless of Default Timezone shenanigans
 *
 * The only implicit conversion is to org.joda.time.LocalDate, all other conversions are lazy and cached
 * The class should be thread safe (worst case scenario - some recalculating is done)
 * but it is intended for one time transformations
 * </pre>
 */
public class JodaLocalDate {

    public LocalDate toOrgJodaTimeLocalDate() {
        return localDate;
    }

    public DateTime toOrgJodaDateTime() {
        if (dateTime == null)
            dateTime = localDate.toDateTimeAtStartOfDay(getDefaultTimeZone());

        return dateTime;
    }

    public long toEpochMillis() {
        return toOrgJodaDateTime().getMillis();
    }

    public Instant toOrgJodaTimeInstant() {
        if (instant == null)
            instant = toOrgJodaDateTime().toInstant();

        return instant;
    }

    public java.time.Instant toJavaTimeInstant() throws ParseException {
        if (javaInstant == null)
            javaInstant = java.time.Instant.ofEpochMilli(toEpochMillis());

        return javaInstant;
    }

    public java.util.Date toJavaUtilDate() throws ParseException {
        if (javaDate == null)
            javaDate = new java.util.Date(toEpochMillis());

        return javaDate;
    }

    public java.sql.Date toJavaSqlDate() throws ParseException {
        if (sqlDate == null)
            sqlDate = java.sql.Date.valueOf(localDate.toString());

        return sqlDate;
    }

    // CONSTRUCTORS

    public static JodaLocalDate from(LocalDate localDate) {
        return new JodaLocalDate(localDate, null, null, null, null, null);
    }

    public static JodaLocalDate from(DateTime dateTime) {
        return new JodaLocalDate(dateTime.withChronology(getDefaultChronology()).toLocalDate(), null, dateTime, null, null, null);
    }

    public static JodaLocalDate from(Instant instant) {
        DateTime dateTime = instant.toDateTime(getDefaultChronology());
        return new JodaLocalDate(dateTime.toLocalDate(), instant, dateTime, null, null, null);
    }

    public static JodaLocalDate from(long epochMillis) {
        return from(new DateTime(epochMillis, getDefaultChronology()));
    }

    public static JodaLocalDate from(java.time.Instant javaInstant) {
        DateTime dateTime = new DateTime(javaInstant.toEpochMilli(), getDefaultChronology());
        return new JodaLocalDate(dateTime.toLocalDate(), null, dateTime, javaInstant, null, null);
    }

    public static JodaLocalDate from(java.util.Date date) throws ParseException {
        if (date instanceof java.sql.Date)
            return from((java.sql.Date) date);

        DateTime dateTime = new DateTime(date.getTime(), getDefaultChronology());
        return new JodaLocalDate(dateTime.toLocalDate(), null, dateTime, null, date, null);
    }

    // Assumes yyyy-MM-dd
    public static JodaLocalDate from(String string) throws ParseException {
        return from(LocalDate.parse(string));
    }

    public static JodaLocalDate from(java.sql.Date sqlDate) throws ParseException {
        return new JodaLocalDate(LocalDate.parse(sqlDate.toString()), null, null, null, null, sqlDate);
    }

    private JodaLocalDate(LocalDate localDate, Instant instant, DateTime dateTime, java.time.Instant javaInstant, java.util.Date javaDate, java.sql.Date sqlDate) {
        this.localDate = localDate;
        this.instant = instant;
        this.dateTime = dateTime;
        this.javaInstant = javaInstant;
        this.javaDate = javaDate;
        this.sqlDate = sqlDate;
    }

    // PRIVATE

    private final LocalDate localDate;
    private Instant instant;
    private DateTime dateTime;
    private java.time.Instant javaInstant;
    private java.util.Date javaDate;
    private java.sql.Date sqlDate;

}
