package com.superum.helper;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeConverter {

    public Instant dateInstant() {
        return instant.truncatedTo(ChronoUnit.DAYS);
    }

    public Date date() {
        return Date.from(dateInstant());
    }

    public int hour() {
        return instant.get(ChronoField.HOUR_OF_DAY);
    }

    public int minute() {
        return instant.get(ChronoField.MINUTE_OF_HOUR);
    }

    public static long time(Date date, int hour, int minute) {
        return time(new TimeConverter(date).dateInstant(), hour, minute);
    }

    public static long time(Instant time, int hour, int minute) {
        return time.plus(hour, ChronoUnit.HOURS).plus(minute, ChronoUnit.MINUTES).toEpochMilli();
    }

    // CONSTRUCTORS

    public TimeConverter(long millis) {
        this.instant = Instant.ofEpochMilli(millis);
    }

    public TimeConverter(Date date) {
        this(date.getTime());
    }

    // PRIVATE

    private final Instant instant;

}
