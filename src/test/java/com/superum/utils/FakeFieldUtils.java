package com.superum.utils;

import org.joda.time.LocalDate;

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

    public static int fakeDay(long id) {
        if (id == 0) return 31;
        if (id < 0) id = -id;

        return (int)((id - 1) % 31) + 1;
    }

    /**
     * @deprecated Date fields should not be used any longer, use Instant/LocalDate instead
     */
    @Deprecated
    public static Date fakeDate(long id) {
        return Date.from(Instant.parse("2015-01-" + dayString(id) + "T00:00:00.00Z"));
    }

    public static String fakeWebsite(long id) {
        return "http://website" + id + ".eu/";
    }

    public static boolean fakeBoolean(long id) {
        return (id & 1) == 1;
    }

    public static String fakeLanguageLevel(long id) {
        return "English: C" + id;
    }

    public static int fakeHour(long id) {
        return (int)(id % 24);
    }

    public static int fakeMinute(long id) {
        return (int)(id % 60);
    }

    public static int fakeId(long id) {
        return (int)id;
    }

    public static LocalDate fakeLocalDate(long id) {
        return LocalDate.parse("2015-01-" + dayString(id));
    }

    public static String dayString(long id) {
        String dayString = String.valueOf(fakeDay(id));
        if (dayString.length() == 1)
            dayString = 0 + dayString;
        return dayString;
    }

    public static String fakeLanguage(long id) {
        if (id < 0) id = -id;
        return "L" + (id % 100);
    }

    public static String fakePassword() {
        return "canYouGuessMe?nah";
    }

    // PRIVATE

    private FakeFieldUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
