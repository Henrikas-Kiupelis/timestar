package com.superum.helper.utils;

import java.math.BigDecimal;

/**
 * <pre>
 * Used to validate certain constraints manually rather than rely on @Valid annotation;
 *
 * Reasons for using this over @Valid:
 * 1) Copying the class for use in other projects, i.e. Android, libraries, etc, becomes cumbersome, because the
 *    dependencies of the annotations must be preserved; in most cases they are simply deleted;
 * 2) The validations seem all too basic, requiring either 3rd party annotations or custom made annotations for
 *    special cases; this seems daft, because it's simpler to just write plain old Java code; not to mention it
 *    is easier to debug this way;
 * 3) Allows for easier customization of error messages returned by the server, since custom exceptions can be
 *    thrown without significant hassle;
 * 4) Allows to test for incorrect input values prematurely as well, rather than waiting until an instance of the
 *    object was created, if such a need arises;
 * </pre>
 */
public final class ValidationUtils {

    /**
     * <pre>
     * Given an int, returns true if this int can be used to represent a day of a month, from 1st to 31st
     *
     * While certain months do not have days 29 to 31, it is assumed that this is handled somewhere else
     * entirely; the purpose of this is simply to validate the input
     * </pre>
     */
    public static boolean isDayOfMonth(int day) {
        return day >= 1 && day <= 31;
    }

    /**
     * <pre>
     * Returns true if a given BigDecimal value is negative, and not null
     *
     * Useful in cases where a BigDecimal JSON field can be optional, but has to be positive if it is present
     * </pre>
     */
    public static boolean isPositiveOrNull(BigDecimal monetaryValue) {
        return monetaryValue == null || isPositive(monetaryValue);
    }

    /**
     * <pre>
     * Returns true if a given BigDecimal value is positive
     *
     * Assumes the BigDecimal is not null (i.e. checked beforehand)
     * </pre>
     */
    public static boolean isPositive(BigDecimal monetaryValue) {
        return monetaryValue.signum() == 1;
    }

    /**
     * <pre>
     * Returns true if the given string is at most as large as the given size (but not empty), or null
     *
     * Useful in cases where a String JSON field can be optional, but has to be no larger than a certain limit
     * if it is present, but empty value is invalid
     * </pre>
     */
    public static boolean fitsOrNullNotEmpty(int size, String string) {
        return string == null || (!string.isEmpty() && fits(size, string));
    }

    /**
     * <pre>
     * Returns true if the given string is at most as large as the given size, or null
     *
     * Useful in cases where a String JSON field can be optional, but has to be no larger than a certain limit
     * if it is present
     * </pre>
     */
    public static boolean fitsOrNull(int size, String string) {
        return string == null || fits(size, string);
    }

    /**
     * <pre>
     * Returns true if the given string is at most as large as the given size, not empty and not null
     *
     * Useful in cases where a List of String JSON field can be optional, but each element in the list in not optional
     * and has to be no larger than a certain limit if it is present, and empty value is invalid
     * </pre>
     */
    public static boolean fitsNotNullNotEmpty(int size, String string) {
        return string != null && !string.isEmpty() && fits(size, string);
    }

    /**
     * <pre>
     * Returns true if the given string is at most as large as the given size, and not null
     *
     * Useful in cases where a List of String JSON field can be optional, but each element in the list in not optional
     * and has to be no larger than a certain limit if it is present
     * </pre>
     */
    public static boolean fitsNotNull(int size, String string) {
        return string != null && fits(size, string);
    }

    /**
     * <pre>
     * Returns true if the given string is at most as large as the given size
     *
     * Assumes the String is not null (i.e. checked beforehand)
     * </pre>
     */
    public static boolean fits(int size, String string) {
        return string.length() <= size;
    }

    // PRIVATE

    private ValidationUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
