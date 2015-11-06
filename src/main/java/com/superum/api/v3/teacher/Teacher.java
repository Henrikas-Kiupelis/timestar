package com.superum.api.v3.teacher;

import com.google.common.base.MoreObjects;
import com.superum.api.v3.account.AccountService;
import com.superum.api.v3.teacher.dto.FetchedTeacher;
import eu.goodlike.libraries.jooq.CommandsMany;
import eu.goodlike.misc.Scaleless;
import eu.goodlike.misc.SpecialUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Teacher {

    public Optional<FetchedTeacher> create() {
        return null;
    }

    public int update() {
        return 0;
    }

    // CONSTRUCTORS

    public Teacher(long createdAt, long updatedAt, Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage,
                   String name, String surname, String phone, String city, String email, String picture, String document,
                   String comment, List<String> languages, TeacherRepository teacherRepository,
                   CommandsMany<Integer, String> teacherLanguageCommands, AccountService validAccountService) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.paymentDay = paymentDay;
        this.hourlyWage = hourlyWage;
        this.academicWage = academicWage;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.picture = picture;
        this.document = document;
        this.comment = comment;
        this.languages = languages;

        this.teacherRepository = teacherRepository;
        this.teacherLanguageCommands = teacherLanguageCommands;
        this.validAccountService = validAccountService;
    }

    // PRIVATE

    private final long createdAt;
    private final long updatedAt;
    private final Integer paymentDay;
    private final BigDecimal hourlyWage;
    private final BigDecimal academicWage;
    private final String name;
    private final String surname;
    private final String phone;
    private final String city;
    private final String email;
    private final String picture;
    private final String document;
    private final String comment;
    private final List<String> languages;

    private final TeacherRepository teacherRepository;
    private final CommandsMany<Integer, String> teacherLanguageCommands;
    private final AccountService validAccountService;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("paymentDay", paymentDay)
                .add("hourlyWage", hourlyWage)
                .add("academicWage", academicWage)
                .add("name", name)
                .add("surname", surname)
                .add("phone", phone)
                .add("city", city)
                .add("email", email)
                .add("picture", picture)
                .add("document", document)
                .add("comment", comment)
                .add("languages", languages)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(createdAt, teacher.createdAt) &&
                Objects.equals(updatedAt, teacher.updatedAt) &&
                Objects.equals(paymentDay, teacher.paymentDay) &&
                SpecialUtils.equals(hourlyWage, teacher.hourlyWage, Scaleless::bigDecimal) &&
                SpecialUtils.equals(academicWage, teacher.academicWage, Scaleless::bigDecimal) &&
                Objects.equals(name, teacher.name) &&
                Objects.equals(surname, teacher.surname) &&
                Objects.equals(phone, teacher.phone) &&
                Objects.equals(city, teacher.city) &&
                Objects.equals(email, teacher.email) &&
                Objects.equals(picture, teacher.picture) &&
                Objects.equals(document, teacher.document) &&
                Objects.equals(comment, teacher.comment) &&
                Objects.equals(languages, teacher.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, paymentDay, Scaleless.bigDecimal(hourlyWage),
                Scaleless.bigDecimal(academicWage), name, surname, phone, city, email, picture, document,
                comment, languages);
    }

}
