package com.superum.api.v3.account.impl;

import com.superum.api.v3.account.AccountEmailSender;
import eu.goodlike.libraries.spring.gmail.GMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEmailSenderImpl implements AccountEmailSender {

    @Override
    public void sendEmailWithCredentials(String email, String partitionName, String password) {
        gMail.send(email, EMAIL_TITLE + partitionName, EMAIL_BODY + password);
    }

    // CONSTRUCTORS

    @Autowired
    public AccountEmailSenderImpl(GMail gMail) {
        this.gMail = gMail;
    }

    // PRIVATE

    private final GMail gMail;

    private static final String EMAIL_TITLE = "Your password for ";
    private static final String EMAIL_BODY = "Password: ";

}
