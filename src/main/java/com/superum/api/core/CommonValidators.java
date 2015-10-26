package com.superum.api.core;

import eu.goodlike.v2.validate.Validate;
import eu.goodlike.v2.validate.impl.IntegerValidator;
import eu.goodlike.v2.validate.impl.LongIntegerValidator;
import eu.goodlike.v2.validate.impl.StringValidator;

import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static eu.goodlike.misc.Constants.NOT_NULL_NOT_BLANK;

public class CommonValidators {

    public static final StringValidator OPTIONAL_JSON_STRING =
            Validate.string().isNull().or().not().isBlank().isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final StringValidator OPTIONAL_JSON_STRING_BLANK_ABLE =
            Validate.string().isNull().or().isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final StringValidator MANDATORY_JSON_STRING = NOT_NULL_NOT_BLANK
            .isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final StringValidator MANDATORY_JSON_STRING_BLANK_ABLE =
            Validate.string().not().isNull().isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final IntegerValidator OPTIONAL_JSON_ID =
            Validate.integer().isNull().or().isAtLeast(1);

    public static final IntegerValidator MANDATORY_JSON_ID =
            Validate.integer().not().isNull().isAtLeast(1);

    public static final LongIntegerValidator OPTIONAL_JSON_LONG_ID =
            Validate.longInt().isNull().or().isAtLeast(1);

    public static final LongIntegerValidator MANDATORY_JSON_LONG_ID =
            Validate.longInt().not().isNull().isAtLeast(1);

    public static final LongIntegerValidator OPTIONAL_TIMESTAMP = Validate.longInt().isNull().or().isAtLeast(0);

    public static final LongIntegerValidator MANDATORY_TIMESTAMP = Validate.longInt().not().isNull().isAtLeast(0);

    // PRIVATE

    private CommonValidators() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
