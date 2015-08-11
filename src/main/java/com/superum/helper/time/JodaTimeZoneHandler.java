package com.superum.helper.time;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * <pre>
 * Converts to and from any of the following formats:
 * org.joda.time.Instant
 * org.joda.time.DateTime;
 * org.joda.time.LocalDate;
 * org.joda.time.LocalTime; (+ hour, minute, second, millisecond)
 * java.util.Instant;
 * java.util.Date;
 * java.sql.Date;
 * epoch milliseconds;
 *
 * Uses epoch milliseconds as the base for converting; uses ISOChronology and the timezone of this handler;
 *
 * When converting to and from LocalDate/LocalTime/SqlDate assumes time 00:00:00.000
 *
 * The handler class itself is thread-safe and immutable, however the Converter class returned by its methods is not;
 * therefore you should NOT cache Converters (the APi supports this by forcing creation of new Converters for different
 * time instances)
 * </pre>
 */
public final class JodaTimeZoneHandler {

    public static DateTimeZone defaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    public JodaTimeConverter from(long epochMillis) {
        return new JodaTimeConverter(epochMillis, null, null, null, null, null, null);
    }

    public JodaTimeConverter from(Instant instant) {
        return new JodaTimeConverter(instant.getMillis(), instant, null, null, null, null, null);
    }

    public JodaTimeConverter from(DateTime dateTime) {
        return new JodaTimeConverter(dateTime.getMillis(), null, dateTime, null, null, null, null);
    }

    /**
     * <pre>
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     * </pre>
     */
    public JodaTimeConverter from(LocalDate localDate) {
        DateTime dateTime = localDate.toDateTimeAtStartOfDay(timeZone);
        return new JodaTimeConverter(dateTime.getMillis(), null, dateTime, localDate, null, null, null);
    }

    public JodaTimeConverter from(LocalDate localDate, LocalTime localTime) {
        DateTime dateTime = localDate.toDateTime(localTime, timeZone);
        return new JodaTimeConverter(dateTime.getMillis(), null, dateTime, localDate, localTime, null, null);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours, minutes, seconds and milliseconds
     *          exceeds a day's worth of time (essentially, overflowing into the next or previous day)
     */
    public JodaTimeConverter from(LocalDate localDate, int hours, int minutes, int seconds, int milliseconds) {
        DateTime dateTimeAtStartOfDay = localDate.toDateTimeAtStartOfDay(timeZone);
        DateTime dateTime = dateTimeAtStartOfDay.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusMillis(milliseconds);

        if (!dateTimeAtStartOfDay.toLocalDate().equals(dateTime.toLocalDate()))
            throw new IllegalArgumentException("The given values exceed a day's worth of time!");

        return new JodaTimeConverter(dateTime.getMillis(), null, dateTime, localDate, null, null, null);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours, minutes and seconds
     *          exceeds a day's worth of time (essentially, overflowing into the next or previous day)
     */
    public JodaTimeConverter from(LocalDate localDate, int hours, int minutes, int seconds) {
        return from(localDate, hours, minutes, seconds, 0);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours and minutes
     *          exceeds a day's worth of time (essentially, overflowing into the next or previous day)
     */
    public JodaTimeConverter from(LocalDate localDate, int hours, int minutes) {
        return from(localDate, hours, minutes, 0, 0);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours
     *          exceeds a day's worth of time (essentially, overflowing into the next or previous day)
     */
    public JodaTimeConverter from(LocalDate localDate, int hours) {
        return from(localDate, hours, 0, 0, 0);
    }

    public JodaTimeConverter from(java.time.Instant instant) {
        return new JodaTimeConverter(instant.toEpochMilli(), null, null, null, null, instant, null);
    }

    /**
     * <pre>
     * This method explicitly checks if the Date instance is a java.sql.Date or not
     * Please refer to "from(java.sql.Date)" method for reasons
     *
     * The assumption behind java.util.Date is that it was created using a long value, i.e.
     * new Date(long);
     * </pre>
     */
    public JodaTimeConverter from(java.util.Date date) {
        if (date instanceof java.sql.Date)
            return from((java.sql.Date) date);

        return new JodaTimeConverter(date.getTime(), null, null, null, null, null, date);
    }

    /**
     * <pre>
     * The assumption behind java.sql.Date is that it was created using a String value, i.e.
     *      Date.parse("yyyy-MM-dd");
     * This uses the JAVA timezone to create a localized instance; therefore, the epoch milliseconds value
     * refers to the start of day at JAVA timezone, and the String value of the Date can get altered during conversion;
     * using toString() method, however, once again uses JAVA timezone, essentially allowing the value to remain correct
     *
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     * </pre>
     */
    public JodaTimeConverter from(java.sql.Date date) {
        return from(date.toString());
    }

    /**
     * <pre>
     * This method expects the following date format:
     *      "yyyy-MM-dd"; for example: "2015-08-05"
     *
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     * </pre>
     */
    public JodaTimeConverter from(String dateString) {
        return from(LocalDate.parse(dateString));
    }

    // CONSTRUCTORS

    public static synchronized JodaTimeZoneHandler forTimeZone(DateTimeZone timeZone) {
        JodaTimeZoneHandler jodaTimeZoneHandler = Retrieval.byZone.get(timeZone);
        if (jodaTimeZoneHandler == null) {
            jodaTimeZoneHandler = new JodaTimeZoneHandler(timeZone);
            Retrieval.byZone.put(timeZone, jodaTimeZoneHandler);
        }
        return jodaTimeZoneHandler;
    }

    public static JodaTimeZoneHandler getDefault() {
        return forTimeZone(defaultTimeZone());
    }

    public static JodaTimeZoneHandler forTimeZoneId(String timeZoneId) {
        return forTimeZone(DateTimeZone.forID(timeZoneId));
    }

    public static JodaTimeZoneHandler forJavaTimeZone(TimeZone timeZone) {
        return forTimeZone(DateTimeZone.forTimeZone(timeZone));
    }

    private JodaTimeZoneHandler(DateTimeZone timeZone) {
        this.timeZone = timeZone;
        this.chronology = ISOChronology.getInstance(timeZone);
    }

    // PRIVATE

    private final DateTimeZone timeZone;
    private final Chronology chronology;

    private static final DateTimeZone DEFAULT_TIME_ZONE = DateTimeZone.forID("UTC");

    private static final class Retrieval {
        private static final Map<DateTimeZone, JodaTimeZoneHandler> byZone = new HashMap<>();
    }

    /**
     * <pre>
     * Converts to and from any of the following formats:
     * org.joda.time.Instant
     * org.joda.time.DateTime;
     * org.joda.time.LocalDate;
     * org.joda.time.LocalTime; (+ hour, minute, second, millisecond)
     * java.util.Instant;
     * java.util.Date;
     * java.sql.Date;
     * epoch milliseconds;
     *
     * Uses epoch milliseconds as the base for converting; uses ISOChronology and the timezone of associated handler;
     *
     * When converting to and from LocalDate/LocalTime/SqlDate assumes time 00:00:00.000
     *
     * This class is not thread safe and should not be cached; it only calculates the appropriate values
     * when needed and stores them for further computations
     * </pre>
     */
    public final class JodaTimeConverter {

        public long toEpochMillis() {
            return epochMillis;
        }

        public Instant toOrgJodaTimeInstant() {
            if (instant == null)
                instant = new Instant(epochMillis);

            return instant;
        }

        public DateTime toOrgJodaTimeDateTime() {
            if (dateTime == null)
                dateTime = new DateTime(epochMillis, chronology);

            return dateTime;
        }

        public LocalDate toOrgJodaTimeLocalDate() {
            if (localDate == null)
                localDate = new LocalDate(epochMillis, chronology);

            return localDate;
        }

        public LocalTime toOrgJodaTimeLocalTime() {
            if (localTime == null)
                localTime = new LocalTime(epochMillis, chronology);

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
                javaInstant = java.time.Instant.ofEpochMilli(epochMillis);

            return javaInstant;
        }

        /**
         * <pre>
         * The assumption behind java.util.Date is that it will be consumed using a long value, i.e.
         * date.getTime();
         * </pre>
         */
        public java.util.Date toJavaUtilDate() {
            if (javaDate == null)
                javaDate = new java.util.Date(epochMillis);

            return javaDate;
        }

        /**
         * <pre>
         * The assumption behind java.sql.Date is that it will be consumed using a String value, i.e.
         * date.toString():
         * This uses the JAVA timezone to create a localized instance; therefore, the epoch milliseconds value
         * refers to the start of day at JAVA timezone, and the String value of the Date can get altered during conversion;
         * using toString() method, however, once again uses JAVA timezone, essentially allowing the value to remain correct
         * </pre>
         */
        public java.sql.Date toJavaSqlDate() {
            if (sqlDate == null)
                sqlDate = java.sql.Date.valueOf(toOrgJodaTimeLocalDate().toString());

            return sqlDate;
        }

        // CONSTRUCTORS

        private JodaTimeConverter(long epochMillis, Instant instant, DateTime dateTime, LocalDate localDate,
                                  LocalTime localTime, java.time.Instant javaInstant, java.util.Date javaDate) {
            this.epochMillis = epochMillis;
            this.instant = instant;
            this.dateTime = dateTime;
            this.localDate = localDate;
            this.localTime = localTime;
            this.javaInstant = javaInstant;
            this.javaDate = javaDate;
        }

        // PRIVATE

        private final long epochMillis;
        private Instant instant;
        private DateTime dateTime;
        private LocalDate localDate;
        private LocalTime localTime;
        private java.time.Instant javaInstant;
        private java.util.Date javaDate;
        private java.sql.Date sqlDate;
    }

}
