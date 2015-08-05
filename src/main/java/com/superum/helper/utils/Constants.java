package com.superum.helper.utils;

/**
 * <pre>
 * Contains various constants, without association to any particular subject
 *
 * It was created to avoid multiple Utils classes just holding a single constant fields
 * </pre>
 */
public class Constants {

    /**
     * Default RestController "produces" and "consumes" value
     */
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    /**
     * Default separator, used by MYSQL when doing group_concat()
     */
    public static final String MYSQL_GROUP_CONCAT_SEPARATOR = ",";

    // PRIVATE

    private Constants() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
