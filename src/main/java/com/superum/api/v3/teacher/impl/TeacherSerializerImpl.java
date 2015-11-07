package com.superum.api.v3.teacher.impl;

import com.superum.api.v3.teacher.TeacherSerializer;
import com.superum.api.v3.teacher.dto.FetchedTeacher;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static timestar_v2.Tables.TEACHER;

@Service
public class TeacherSerializerImpl implements TeacherSerializer {

    @Override
    public FetchedTeacher toReturnable(Record record) {
        if (record == null)
            return null;

        int id = record.getValue(TEACHER.ID);
        long createdAt = record.getValue(TEACHER.CREATED_AT);
        long updatedAt = record.getValue(TEACHER.UPDATED_AT);
        Integer paymentDay = record.getValue(TEACHER.PAYMENT_DAY);
        BigDecimal hourlyWage = record.getValue(TEACHER.HOURLY_WAGE);
        BigDecimal academicWage = record.getValue(TEACHER.ACADEMIC_WAGE);
        String name = record.getValue(TEACHER.NAME);
        String surname = record.getValue(TEACHER.SURNAME);
        String phone = record.getValue(TEACHER.PHONE);
        String city = record.getValue(TEACHER.CITY);
        String email = record.getValue(TEACHER.EMAIL);
        String picture = record.getValue(TEACHER.PICTURE);
        String document = record.getValue(TEACHER.DOCUMENT);
        String comment = record.getValue(TEACHER.COMMENT);
        List<String> languages = teacherLanguagesField.valueFor(record);
        return new FetchedTeacher(id, createdAt, updatedAt, paymentDay, hourlyWage, academicWage, name, surname, phone,
                city, email, picture, document, comment, languages);
    }

    // CONSTRUCTORS

    @Autowired
    public TeacherSerializerImpl(TeacherLanguagesField teacherLanguagesField) {
        this.teacherLanguagesField = teacherLanguagesField;
    }

    // PRIVATE

    private final TeacherLanguagesField teacherLanguagesField;

}
