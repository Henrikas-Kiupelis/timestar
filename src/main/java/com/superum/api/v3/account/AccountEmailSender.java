package com.superum.api.v3.account;

public interface AccountEmailSender {

    void sendEmailWithCredentials(String email, String partitionName, String password);

}
