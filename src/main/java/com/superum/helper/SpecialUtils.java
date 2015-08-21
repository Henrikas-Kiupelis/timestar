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

    /**
     * <pre>
     * This method is useful when BigDecimals can come from different sources, which may choose to trim the scale
     * of their BigDecimals; using normal equals() method would imply that 10.0000 != 10 != 10.00, etc
     * In this method, however, 10.0000 == 10 == 10.00, etc
     *
     * It is important to note, that if you use this method to compare two BigDecimals in an equals() method
     * of some class, you need to also use a customized hashCode() implementation; it is suggested to use
     * Double.valueOf(bigDecimal.doubleValue()).hashCode();
     * </pre>
     * @return true if the given BigDecimals are equal, ignoring scale; false otherwise
     */
    public static boolean equalsJavaMathBigDecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1 == null ? bigDecimal2 == null : bigDecimal1.compareTo(bigDecimal2) == 0;
    }

    public static int compareNullableIntegers(Integer i1, Integer i2) {
        return i1 == null
                        ? 1
                        : i2 == null
                                ? -1
                                : Integer.compare(i1, i2);
    }

    // PRIVATE

    private SpecialUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
