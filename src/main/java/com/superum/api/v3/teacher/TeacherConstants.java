package com.superum.api.v3.teacher;

public final class TeacherConstants {

    public static final String ID_FIELD = "id";
    public static final String CREATED_AT_FIELD = "createdAt";
    public static final String UPDATED_AT_FIELD = "updatedAt";
    public static final String PAYMENT_DAY_FIELD = "paymentDay";
    public static final String HOURLY_WAGE_FIELD = "hourlyWage";
    public static final String ACADEMIC_WAGE_FIELD = "academicWage";
    public static final String NAME_FIELD = "name";
    public static final String SURNAME_FIELD = "surname";
    public static final String PHONE_FIELD = "phone";
    public static final String CITY_FIELD = "city";
    public static final String EMAIL_FIELD = "email";
    public static final String PICTURE_FIELD = "picture";
    public static final String DOCUMENT_FIELD = "document";
    public static final String COMMENT_FIELD = "comment";
    public static final String LANGUAGES_FIELD = "languages";

    public static final int COMMENT_SIZE_LIMIT = 500;
    public static final int LANGUAGE_CODE_SIZE_LIMIT = 3;

    // PRIVATE

    private TeacherConstants() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }


}
