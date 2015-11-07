package com.superum.api.v3.account;

public interface AccountServiceExt {

    void registerTeacher(int id, String email);

    void updateTeacherEmail(String originalEmail, String newEmail);

}
