package com.superum.api.v3.teacher;

import com.google.common.base.MoreObjects;
import com.superum.api.v3.account.AccountServiceExt;
import com.superum.api.v3.teacher.dto.FetchedTeacher;
import eu.goodlike.libraries.jooq.CommandsMany;
import eu.goodlike.libraries.jooq.Queries;
import eu.goodlike.misc.Scaleless;
import eu.goodlike.misc.SpecialUtils;
import timestar_v2.tables.records.TeacherRecord;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static timestar_v2.Tables.TEACHER;

public class Teacher {

    public Optional<FetchedTeacher> create() {
        return teacherRepository.create(paymentDay, hourlyWage, academicWage, name, surname, phone,
                city, email, picture, document, comment, createdAt.toEpochMilli(), updatedAt.toEpochMilli())
                .flatMap(this::finishCreation);
    }

    public int update(int id) {
        String originalEmail = teacherQueries.readField(TEACHER.EMAIL, id)
                .orElseThrow(() -> TeacherErrors.teacherIdError(id));
        int teacherRows = teacherRepository.update(id, paymentDay, hourlyWage, academicWage, name, surname, phone,
                city, email, picture, document, comment, createdAt.toEpochMilli(), updatedAt.toEpochMilli());
        if (teacherRows == 0)
            return 0;

        if (email != null) {
            if (originalEmail == null)
                accountServiceExt.registerTeacher(id, email);
            else
                accountServiceExt.updateTeacherEmail(originalEmail, email);
        }
        teacherLanguageCommands.update(id, languages);
        return teacherRows;
    }

    // CONSTRUCTORS

    public Teacher(Instant createdAt, Instant updatedAt, Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage,
                   String name, String surname, String phone, String city, String email, String picture, String document,
                   String comment, List<String> languages, TeacherRepository teacherRepository,
                   Queries<TeacherRecord, Integer> teacherQueries, CommandsMany<Integer, String> teacherLanguageCommands,
                   AccountServiceExt accountServiceExt, TeacherSerializer teacherSerializer) {
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
        this.teacherQueries = teacherQueries;
        this.teacherLanguageCommands = teacherLanguageCommands;
        this.accountServiceExt = accountServiceExt;
        this.teacherSerializer = teacherSerializer;
    }

    // PRIVATE

    private final Instant createdAt;
    private final Instant updatedAt;
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
    private final Queries<TeacherRecord, Integer> teacherQueries;
    private final CommandsMany<Integer, String> teacherLanguageCommands;
    private final AccountServiceExt accountServiceExt;
    private final TeacherSerializer teacherSerializer;

    private Optional<FetchedTeacher> finishCreation(int id) {
        if (email != null)
            accountServiceExt.registerTeacher(id, email);

        teacherLanguageCommands.create(id, languages);
        return teacherQueries.read(id, teacherSerializer::toReturnable);
    }

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
