package com.superum.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class FakeFieldUtils {

    public static String fakeName(long id) {
        return "Name" + id;
    }

    public static String fakeSurname(long id) {
        return "Surname" + id;
    }

    public static String fakeEmail(long id) {
        return "fake" + id + "@fake.lt";
    }

    public static String fakeCity(long id) {
        return "City" + id;
    }

    public static String fakePhone(long id) {
        return "860000000" + id;
    }

    public static String fakePicture(long id) {
        return "Picture" + id + ".jpg";
    }

    public static String fakeDocument(long id) {
        return "Document" + id;
    }

    public static String fakeComment(long id) {
        return "Comment" + id;
    }

    public static BigDecimal fakeWage(long id) {
        return BigDecimal.valueOf(id);
    }

    public static long fakeDay(long id) {
        return (id % 31) + 1;
    }

    public static Date fakeDate(long id) {
        String dayString = String.valueOf(fakeDay(id));
        if (dayString.length() == 1)
            dayString = 0 + dayString;
        return Date.from(Instant.parse("2015-01-" + dayString + "T00:00:00.00Z"));
    }

    public static String fakeWebsite(long id) {
        return "http://website" + id + ".eu/";
    }

    public static boolean fakeBoolean(long id) {
        return (id & 1) == 1;
    }

}
