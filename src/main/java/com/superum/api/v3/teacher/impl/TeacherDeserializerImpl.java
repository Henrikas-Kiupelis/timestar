package com.superum.api.v3.teacher.impl;

import com.superum.api.v3.account.AccountServiceExt;
import com.superum.api.v3.teacher.*;
import com.superum.api.v3.teacher.dto.FetchedTeacher;
import com.superum.api.v3.teacher.dto.SuppliedTeacher;
import eu.goodlike.libraries.jooq.CommandsMany;
import eu.goodlike.libraries.jooq.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.TeacherRecord;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static timestar_v2.Tables.TEACHER;

@Service
public class TeacherDeserializerImpl implements TeacherDeserializer {

    @Override
    public Teacher toCreatable(SuppliedTeacher suppliedTeacher) {
        suppliedTeacher.validateForCreation();

        String email = suppliedTeacher.getEmail();
        assertUniqueEmail(email);

        Integer paymentDay = suppliedTeacher.getPaymentDay();
        BigDecimal hourlyWage = suppliedTeacher.getHourlyWage();
        BigDecimal academicWage = suppliedTeacher.getAcademicWage();
        String name = suppliedTeacher.getName();
        String surname = suppliedTeacher.getSurname();
        String phone = suppliedTeacher.getPhone();
        String city = suppliedTeacher.getCity();
        String picture = suppliedTeacher.getPicture();
        String document = suppliedTeacher.getDocument();
        String comment = suppliedTeacher.getComment();
        List<String> languages = suppliedTeacher.getLanguages();

        Instant now = Instant.now();
        return Teacher.valueOf(now, now, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture,
                document, comment, languages, teacherRepository, teacherQueries, teacherLanguageCommands,
                accountServiceExt, teacherSerializer);
    }

    @Override
    public Teacher toUpdatable(SuppliedTeacher suppliedTeacher, int id) {
        suppliedTeacher.validateForUpdating();

        FetchedTeacher fetchedTeacher = teacherQueries.read(id, teacherSerializer::toReturnable)
                .orElseThrow(() -> TeacherErrors.teacherIdError(id));

        String email = suppliedTeacher.getEmail();
        assertUniqueEmail(email, id);

        Integer paymentDay = suppliedTeacher.getPaymentDay();
        BigDecimal hourlyWage = suppliedTeacher.getHourlyWage();
        BigDecimal academicWage = suppliedTeacher.getAcademicWage();
        String name = suppliedTeacher.getName();
        String surname = suppliedTeacher.getSurname();
        String phone = suppliedTeacher.getPhone();
        String city = suppliedTeacher.getCity();
        String picture = suppliedTeacher.getPicture();
        String document = suppliedTeacher.getDocument();
        String comment = suppliedTeacher.getComment();
        List<String> languages = suppliedTeacher.getLanguages();

        if (paymentDay == null) paymentDay = fetchedTeacher.getPaymentDay();
        if (hourlyWage == null) hourlyWage = fetchedTeacher.getHourlyWage();
        if (academicWage == null) academicWage = fetchedTeacher.getAcademicWage();
        if (name == null) name = fetchedTeacher.getName();
        if (surname == null) surname = fetchedTeacher.getSurname();
        if (phone == null) phone = fetchedTeacher.getPhone();
        if (city == null) city = fetchedTeacher.getCity();
        if (email == null) email = fetchedTeacher.getEmail();
        if (picture == null) picture = fetchedTeacher.getPicture();
        if (document == null) document = fetchedTeacher.getDocument();
        if (comment == null) comment = fetchedTeacher.getComment();
        if (languages == null) languages = fetchedTeacher.getLanguages();

        Instant createdAt = Instant.ofEpochMilli(fetchedTeacher.getCreatedAt());
        Instant updatedAt = Instant.now();
        return Teacher.valueOf(createdAt, updatedAt, paymentDay, hourlyWage, academicWage, name, surname, phone, city,
                email, picture, document, comment, languages, teacherRepository, teacherQueries,
                teacherLanguageCommands, accountServiceExt, teacherSerializer);
    }

    // CONSTRUCTORS

    @Autowired
    public TeacherDeserializerImpl(TeacherRepository teacherRepository, Queries<TeacherRecord, Integer> teacherQueries,
                                   CommandsMany<Integer, String> teacherLanguageCommands,
                                   AccountServiceExt accountServiceExt, TeacherSerializer teacherSerializer) {
        this.teacherRepository = teacherRepository;
        this.teacherQueries = teacherQueries;
        this.teacherLanguageCommands = teacherLanguageCommands;
        this.accountServiceExt = accountServiceExt;
        this.teacherSerializer = teacherSerializer;
    }

    // PRIVATE

    private final TeacherRepository teacherRepository;
    private final Queries<TeacherRecord, Integer> teacherQueries;
    private final CommandsMany<Integer, String> teacherLanguageCommands;
    private final AccountServiceExt accountServiceExt;
    private final TeacherSerializer teacherSerializer;

    private void assertUniqueEmail(String email) {
        assertUniqueEmail(email, 0);
    }

    private void assertUniqueEmail(String email, int id) {
        if (email != null && teacherQueries.exists(TEACHER.EMAIL.eq(email).and(TEACHER.ID.ne(id))))
            throw TeacherErrors.duplicateEmailError(email);
    }

}
