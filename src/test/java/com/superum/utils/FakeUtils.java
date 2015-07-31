package com.superum.utils;

import com.superum.db.teacher.Teacher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FakeUtils {

    public static List<Teacher> makeSomeFakeTeachers(int amount) {
        List<Teacher> fakeTeachers = new ArrayList<>(amount);
        for (int i = 1; i <= amount; i++)
            fakeTeachers.add(makeFakeTeacher(i));

        return fakeTeachers;
    }

	public static Teacher makeFakeTeacher(int id) {
        int paymentDay = fakePaymentDay(id);
        BigDecimal hourlyWage = fakeWage(id);
        BigDecimal academicWage = fakeWage(id);
        String name = fakeName(id);
        String surname = fakeSurname(id);
        String email = fakeEmail(id);
        String city= fakeCity(id);
        String phone = fakePhone(id);
        String pictureName = fakePictureName(id);
        String documentName  = fakeDocumentName(id);
        String comment = fakeComment(id);

        return makeFakeTeacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone,  city, email, pictureName, documentName, comment);
    }

    public static Teacher makeFakeTeacher(int id, int paymentDay, BigDecimal hourlyWage,  BigDecimal academicWage,
                                          String name, String surname, String phone, String city, String email, String pictureName, String documentName, String comment) {
        return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone,  city, email, pictureName, documentName, comment);
    }

    public static BigDecimal fakeWage(int x) {
        return BigDecimal.valueOf(x);
    }

    public static String fakeName(int x){
        return "Name" + x;
    }

    public static String fakeSurname(int x){
        return "Surname" + x;
    }

    public static String fakeEmail(int x){
        return "fake" + x + "@fake.lt";
    }

    public static String fakeCity(int x){
        return "City" + x;
    }

    public static String fakePhone(int x){
        return "860000000" + x;
    }

    public static String fakePictureName(int x){
        return "untitled" + x + ".jpg";
    }

    public static String fakeDocumentName(int x){
        return "folder" + x;
    }

    public static String fakeComment(int x){
        return "Photo comment" + x;
    }

    public static int fakePaymentDay(int x){
        return (x % 31) + 1;
    }
}
