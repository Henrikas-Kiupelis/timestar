package com.superum.api.v3.account;

import com.superum.api.v1.account.AccountType;

public interface AccountServiceExt {

    void registerTeacher(int id, String email);

    void updateTeacherEmail(String originalEmail, String newEmail);

    void deleteAccount(int id, AccountType accountType);

}
