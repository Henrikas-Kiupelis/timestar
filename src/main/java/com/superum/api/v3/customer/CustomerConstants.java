package com.superum.api.v3.customer;

public final class CustomerConstants {

    public static final String ID_FIELD = "id";
    public static final String START_DATE_FIELD = "startDate";
    public static final String NAME_FIELD = "name";
    public static final String PHONE_FIELD = "phone";
    public static final String WEBSITE_FIELD = "website";
    public static final String PICTURE_FIELD = "picture";
    public static final String COMMENT_FIELD = "comment";
    public static final String CREATED_AT_FIELD = "createdAt";
    public static final String UPDATED_AT_FIELD = "updatedAt";

    public static final int COMMENT_SIZE_LIMIT = 500;

    // PRIVATE

    private CustomerConstants() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }


}
