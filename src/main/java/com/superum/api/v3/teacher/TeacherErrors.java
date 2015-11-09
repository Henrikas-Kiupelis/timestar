package com.superum.api.v3.teacher;

import com.superum.api.v2.teacher.DuplicateTeacherException;
import com.superum.api.v2.teacher.InvalidTeacherException;
import com.superum.api.v2.teacher.TeacherNotFoundException;

import java.math.BigDecimal;
import java.util.List;

import static com.superum.api.v3.teacher.TeacherConstants.COMMENT_SIZE_LIMIT;
import static com.superum.api.v3.teacher.TeacherConstants.LANGUAGE_CODE_SIZE_LIMIT;
import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;

public final class TeacherErrors {

    public static InvalidTeacherException paymentDayError(Integer paymentDay) {
        return new InvalidTeacherException("Payment day should be a day of month, not: " + paymentDay);
    }

    public static InvalidTeacherException hourlyWageError(BigDecimal hourlyWage) {
        return new InvalidTeacherException("Hourly wage for teacher must be positive, not " + hourlyWage);
    }

    public static InvalidTeacherException academicWageError(BigDecimal academicWage) {
        return new InvalidTeacherException("Academic wage for teacher must be positive, not " + academicWage);
    }

    public static InvalidTeacherException nameError(String name) {
        return new InvalidTeacherException("Teacher name must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + name);
    }

    public static InvalidTeacherException surnameError(String surname) {
        return new InvalidTeacherException("Teacher surname must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + surname);
    }

    public static InvalidTeacherException phoneError(String phone) {
        return new InvalidTeacherException("Teacher phone must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + phone);
    }

    public static InvalidTeacherException cityError(String city) {
        return new InvalidTeacherException("Teacher city must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + city);
    }

    public static InvalidTeacherException emailError(String email) {
        return new InvalidTeacherException("Teacher email must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars, be blank or be of invalid format: " + email);
    }

    public static InvalidTeacherException pictureError(String picture) {
        return new InvalidTeacherException("Teacher picture must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + picture);
    }

    public static InvalidTeacherException documentError(String document) {
        return new InvalidTeacherException("Teacher document must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + document);
    }

    public static InvalidTeacherException commentError(String comment) {
        return new InvalidTeacherException("Teacher comment must not exceed " +
                COMMENT_SIZE_LIMIT + " chars: " + comment);
    }

    public static InvalidTeacherException languageCodeError(String languageCode) {
        return new InvalidTeacherException("Specific teacher languages must not be null, blank or exceed " +
                LANGUAGE_CODE_SIZE_LIMIT + " chars: " + languageCode);
    }

    public static InvalidTeacherException languagesError(List<String> languages) {
        return new InvalidTeacherException("Teacher must have at least a single language: " + languages);
    }

    public static TeacherNotFoundException teacherIdError(int id) {
        return new TeacherNotFoundException("Couldn't find teacher with id: " + id);
    }

    public static DuplicateTeacherException duplicateEmailError(String email) {
        return new DuplicateTeacherException("This email is already taken: " + email);
    }

    // PRIVATE

    private TeacherErrors() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
