package com.superum.helper;

import org.joda.time.*;

import java.text.ParseException;

import static com.superum.utils.TimeUtils.*;

/**
 * <pre>
 * Customized time conversion class
 *
 * Intended for epoch timestamp conversions
 *
 * DO NOT USE WITH DATE STRING VALUES LIKE
 * "2015-08-03"
 * INSTEAD, USE JodaLocalDate
 *
 * The resulting Instants/Dates/etc will assume UTC
 *
 * The only implicit conversion is to org.joda.time.Instant, all other conversions are lazy and cached
 * The class is NOT thread safe and is intended for one time multiple transformations
 * </pre>
 */
public class JodaTimeConverter {

    public Instant toOrgJodaTimeInstant() {
        return instant;
    }

    public long toEpochMillis() {
        return instant.getMillis();
    }

    public DateTime toOrgJodaTimeDateTime() {
        if (dateTime == null)
            dateTime = instant.toDateTime(getDefaultChronology());

        return dateTime;
    }

    public LocalDate toOrgJodaTimeLocalDate() {
        if (localDate == null)
            localDate = toOrgJodaTimeDateTime().toLocalDate();

        return localDate;
    }

    public LocalTime toOrgJodaTimeLocalTime() {
        if (localTime == null)
            localTime = toOrgJodaTimeDateTime().toLocalTime();

        return localTime;
    }

    public int toHours() {
        return toOrgJodaTimeLocalTime().getHourOfDay();
    }

    public int toMinutes() {
        return toOrgJodaTimeLocalTime().getMinuteOfHour();
    }

    public int toSeconds() {
        return toOrgJodaTimeLocalTime().getSecondOfMinute();
    }

    public int toMilliseconds() {
        return toOrgJodaTimeLocalTime().getMillisOfSecond();
    }

    public java.time.Instant toJavaTimeInstant() {
        if (javaInstant == null)
            javaInstant = java.time.Instant.ofEpochMilli(toEpochMillis());

        return javaInstant;
    }

    public java.util.Date toJavaUtilDate() {
        if (javaDate == null)
            javaDate = java.util.Date.from(toJavaTimeInstant());

        return javaDate;
    }

    public java.sql.Date toJavaSqlDate() {
        if (sqlDate == null)
            sqlDate = new java.sql.Date(toJavaUtilDate().getTime());

        return sqlDate;
    }

    // CONSTRUCTORS

    public static JodaTimeConverter from(Instant instant) {
        return new JodaTimeConverter(instant, null, null, null, null, null, null);
    }

    public static JodaTimeConverter from(long epochMillis) {
        return from(new Instant(epochMillis));
    }

    public static JodaTimeConverter from(DateTime dateTime) {
        return new JodaTimeConverter(dateTime.withChronology(getDefaultChronology()).toInstant(), dateTime, null, null, null, null, null);
    }

    public static JodaTimeConverter from(LocalDate localDate) {
        DateTime dateTime = localDate.toDateTimeAtStartOfDay(getDefaultTimeZone());
        return new JodaTimeConverter(dateTime.toInstant(), dateTime, localDate, null, null, null, null);
    }

    public static JodaTimeConverter from(LocalDate localDate, LocalTime localTime) {
        DateTime dateTime = localDate.toDateTime(localTime, getDefaultTimeZone());
        return new JodaTimeConverter(dateTime.toInstant(), dateTime, localDate, localTime, null, null, null);
    }

    public static JodaTimeConverter from(LocalDate localDate, int hours, int minutes, int seconds, int milliseconds) {
        Duration totalDuration = Duration.standardHours(hours)
                .plus(Duration.standardMinutes(minutes))
                .plus(Duration.standardSeconds(seconds))
                .plus(Duration.millis(milliseconds));

        if (totalDuration.isLongerThan(Duration.standardDays(1)))
            throw new IllegalArgumentException("The given values exceed a day's worth of time; please adjust the date manually!");

        DateTime dateTime = localDate.toDateTimeAtStartOfDay(getDefaultTimeZone()).plus(totalDuration);
        return new JodaTimeConverter(dateTime.toInstant(), dateTime, null, null, null, null, null);
    }

    public static JodaTimeConverter from(LocalDate localDate, int hours, int minutes, int seconds) {
        return from(localDate, hours, minutes, seconds, 0);
    }

    public static JodaTimeConverter from(LocalDate localDate, int hours, int minutes) {
        return from(localDate, hours, minutes, 0, 0);
    }

    public static JodaTimeConverter from(LocalDate localDate, int hours) {
        return from(localDate, hours, 0, 0, 0);
    }

    public static JodaTimeConverter from(java.time.Instant instant) {
        return new JodaTimeConverter(new Instant(instant.toEpochMilli()), null, null, null, instant, null, null);
    }

    public static JodaTimeConverter from(java.util.Date date) throws ParseException {
        if (date instanceof java.sql.Date)
            return from((java.sql.Date) date);

        java.time.Instant instant = date.toInstant();
        return new JodaTimeConverter(new Instant(instant.toEpochMilli()), null, null, null, instant, date, null);
    }

    public static JodaTimeConverter from(java.sql.Date sqldate) throws ParseException {
        java.util.Date date = fromString(sqldate.toString());
        java.time.Instant instant = date.toInstant();
        return new JodaTimeConverter(new Instant(instant.toEpochMilli()), null, null, null, instant, date, sqldate);
    }

    private JodaTimeConverter(Instant instant, DateTime dateTime, LocalDate localDate, LocalTime localTime, java.time.Instant javaInstant, java.util.Date javaDate, java.sql.Date sqlDate) {
        this.instant = instant;
        this.dateTime = dateTime;
        this.localDate = localDate;
        this.localTime = localTime;
        this.javaInstant = javaInstant;
        this.javaDate = javaDate;
        this.sqlDate = sqlDate;
    }

    // PRIVATE

    private final Instant instant;
    private DateTime dateTime;
    private LocalDate localDate;
    private LocalTime localTime;
    private java.time.Instant javaInstant;
    private java.util.Date javaDate;
    private java.sql.Date sqlDate;

}
