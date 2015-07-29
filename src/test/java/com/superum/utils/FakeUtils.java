package com.superum.utils;

import com.superum.db.teacher.Teacher;

public class FakeUtils {

	public static Teacher makeFakeTeacher(int id, int partitionId){
        String name = fakeName(id, partitionId);
        String surname = fakeSurname(id, partitionId);
        String email = fakeEmail(id, partitionId);
        String city= fakeCity(id, partitionId);
        String phone = fakePhone(id, partitionId);
        String pictureName = fakePictureName(id, partitionId);
        String documentName  = fakeDocumentName(id, partitionId);
        String comment = fakeComment(id, partitionId);
        Byte paymentDay = fakePaymentDay(id, partitionId);
        return makeFakeTeacher(id, paymentDay, name, surname, phone,  city, email, pictureName, documentName, comment);
    }

    public static Teacher makeFakeTeacher(int id, Byte paymentDay, String name, String surname, String phone, String city, String email, String pictureName, String documentName, String comment) {
        return new Teacher(id, paymentDay, name, surname, phone,  city, email, pictureName, documentName, comment);
    }



    public static String fakeName(int x, int y){
        return "Name" + x + " " + y;
    }

    public static String fakeSurname(int x, int y){
        return "Surname" + x + " " + y;
    }

    public static String fakeEmail(int x, int y){
        return "fake" + x + "@fake.lt" + " " + y;
    }

    public static String fakeCity(int x, int y){
        return "City" + x + " " + y;
    }

    public static String fakePhone(int x, int y){
        return "860000000" + x + " " + y;
    }

    public static String fakePictureName(int x, int y){
        return "untitled" + x + ".jpg " + y;
    }

    public static String fakeDocumentName(int x, int y){
        return "folder" + x + " " + y;
    }

    public static String fakeComment(int x, int y){
        return "Photo comment" + x + " " + y;
    }

    public static Byte fakePaymentDay(int x, int y){
        return Byte.MAX_VALUE;
    }
}
