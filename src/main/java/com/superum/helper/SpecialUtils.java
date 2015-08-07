package com.superum.helper;

import java.math.BigDecimal;

/**
 * <pre>
 * Contains various utility methods, without association to any particular subject
 *
 * It was created to avoid multiple Utils classes just holding a single method, which doesn't easily translate
 * into a helper class
 * </pre>
 */
public class SpecialUtils {

    public static boolean equalsJavaMathBigDecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1 == null ? bigDecimal2 == null : bigDecimal1.compareTo(bigDecimal2) == 0;
    }

    // PRIVATE

    private SpecialUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
