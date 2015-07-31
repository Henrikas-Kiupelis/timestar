package com.superum.helper;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeConverter {

    public Date date() {
        return Date.from(instant.truncatedTo(ChronoUnit.DAYS));
    }

    public int hour() {
        return instant.get(ChronoField.HOUR_OF_DAY);
    }

    public int minute() {
        return instant.get(ChronoField.MINUTE_OF_HOUR);
    }

    public static long time(Date date, int hour, int minute) {
        return date.getTime() + (hour * 60 + minute) * 60000;
    }

    // CONSTRUCTORS

    public TimeConverter(long millis) {
        this.instant = Instant.ofEpochMilli(millis);
    }

    // PRIVATE

    private final Instant instant;

}
