package com.superum.api.v3.teacher;

import java.math.BigDecimal;
import java.util.Optional;

public interface TeacherRepository {

    Optional<Integer> create(Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name, String surname,
                             String phone, String city, String email, String picture, String document, String comment, long createdAt,
                             long updatedAt);

    int update(int id, Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name, String surname,
               String phone, String city, String email, String picture, String document, String comment, long updatedAt);

    int delete(int id);

}
