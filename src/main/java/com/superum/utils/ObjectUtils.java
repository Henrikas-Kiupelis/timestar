package com.superum.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.temporal.ChronoUnit;

public class ObjectUtils {

    public static int hash(Object... objects) {
        return hashPart(17, objects);
    }

    public static int hashPart(int init, Object... objects) {
        for (Object o : objects)
            init = (init << 5) - init + (o == null ? 0 : o.hashCode());

        return init;
    }

    public static boolean equalsJavaSqlDate(Date date1, Date date2) {
        return date1 == null ? date2 == null : date1.toString().equals(date2.toString());
    }

    public static boolean equalsJavaUtilDate(java.util.Date date1, java.util.Date date2) {
        return date1 == null ? date2 == null : date1.toInstant().truncatedTo(ChronoUnit.DAYS).equals(date2.toInstant().truncatedTo(ChronoUnit.DAYS));
    }

    public static boolean equalsJavaMathBigDecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1 == null ? bigDecimal2 == null : bigDecimal1.compareTo(bigDecimal2) == 0;
    }

}
