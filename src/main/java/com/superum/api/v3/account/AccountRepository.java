package com.superum.api.v3.account;

public interface AccountRepository {

    int create(Integer id, String username, String password, String accountType, long createdAt, long updatedAt);

}
