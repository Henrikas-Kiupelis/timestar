package com.superum.api.v3.teacher.impl;

import com.superum.api.v3.teacher.TeacherRepository;
import com.superum.helper.PartitionAccount;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.TeacherRecord;

import java.math.BigDecimal;
import java.util.Optional;

import static timestar_v2.Tables.TEACHER;

@Repository
@Transactional
public class TeacherRepositoryImpl implements TeacherRepository {

    @Override
    public Optional<Integer> create(Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name,
                                    String surname, String phone, String city, String email, String picture, String document,
                                    String comment, long createdAt, long updatedAt) {
        return sql.insertInto(TEACHER)
                .set(TEACHER.PARTITION_ID, new PartitionAccount().partitionId())
                .set(TEACHER.PAYMENT_DAY, paymentDay)
                .set(TEACHER.HOURLY_WAGE, hourlyWage)
                .set(TEACHER.ACADEMIC_WAGE, academicWage)
                .set(TEACHER.NAME, name)
                .set(TEACHER.SURNAME, surname)
                .set(TEACHER.PHONE, phone)
                .set(TEACHER.CITY, city)
                .set(TEACHER.EMAIL, email)
                .set(TEACHER.PICTURE, picture)
                .set(TEACHER.DOCUMENT, document)
                .set(TEACHER.COMMENT, comment)
                .set(TEACHER.CREATED_AT, createdAt)
                .set(TEACHER.UPDATED_AT, updatedAt)
                .returning(TEACHER.ID)
                .fetch().stream().findAny()
                .map(TeacherRecord::getId);
    }

    @Override
    public int update(int id, Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name,
                      String surname, String phone, String city, String email, String picture, String document,
                      String comment, long createdAt, long updatedAt) {
        return sql.update(TEACHER)
                .set(TEACHER.PAYMENT_DAY, paymentDay)
                .set(TEACHER.HOURLY_WAGE, hourlyWage)
                .set(TEACHER.ACADEMIC_WAGE, academicWage)
                .set(TEACHER.NAME, name)
                .set(TEACHER.SURNAME, surname)
                .set(TEACHER.PHONE, phone)
                .set(TEACHER.CITY, city)
                .set(TEACHER.EMAIL, email)
                .set(TEACHER.PICTURE, picture)
                .set(TEACHER.DOCUMENT, document)
                .set(TEACHER.COMMENT, comment)
                .set(TEACHER.CREATED_AT, createdAt)
                .set(TEACHER.UPDATED_AT, updatedAt)
                .where(primaryKey(id))
                .execute();
    }

    @Override
    public int delete(int id) {
        return sql.deleteFrom(TEACHER)
                .where(primaryKey(id))
                .execute();
    }

    // CONSTRUCTORS

    @Autowired
    public TeacherRepositoryImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

    private Condition primaryKey(int id) {
        return TEACHER.ID.eq(id).and(TEACHER.PARTITION_ID.eq(new PartitionAccount().partitionId()));
    }

}
