package com.superum.utils;

import java.sql.Date;

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
        return date1.toString().equals(date2.toString());
    }
}
