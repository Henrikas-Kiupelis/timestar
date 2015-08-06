package com.superum.helper.utils;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class EqualsUtils {

    /**
     * @deprecated Please use org.joda.time.LocalDate instead!
     */
    @Deprecated
    public static boolean equalsJavaSqlDate(java.sql.Date date1, java.sql.Date date2) {
        return date1 == null ? date2 == null : date1.toString().equals(date2.toString());
    }

    /**
     * @deprecated Please use org.joda.time.Instant instead!
     */
    @Deprecated
    public static boolean equalsJavaUtilDate(Date date1, Date date2) {
        return date1 == null ? date2 == null : date1.toInstant().truncatedTo(ChronoUnit.DAYS).equals(date2.toInstant().truncatedTo(ChronoUnit.DAYS));
    }

    public static boolean equalsJavaMathBigDecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1 == null ? bigDecimal2 == null : bigDecimal1.compareTo(bigDecimal2) == 0;
    }

    // PRIVATE

    private EqualsUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
