package com.superum.utils;

import com.superum.api.customer.FullCustomer;
import com.superum.db.teacher.Teacher;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;

public class FakeUtils {

    public static FullCustomer defaultFullCustomer() {
        return FullCustomer.newRequiredBuilder()
                .withPaymentDay(1)
                .withStartDate(Date.valueOf("2015-07-21"))
                .withPaymentValue(BigDecimal.valueOf(10))
                .withName("SUPERUM")
                .withPhone("+37069900001")
                .withWebsite("http://superum.eu/")
                .withLanguages(Arrays.asList("English: C1", "English: C2"))
                .withPictureName("pic.jpg")
                .withComment("company test")
                .build();
    }

    public static Teacher defaultTeacher() {
        return new Teacher(
                0,
                (byte)1,
                "Dude",
                "Dudeling",
                "+37069900011",
                "Vilnius",
                "asdbasydasbdu@bujasdbndnuasd.yuidaghsud",
                "picT.jpg",
                "doc.jpg",
                "what a teacher");
    }
	
}
