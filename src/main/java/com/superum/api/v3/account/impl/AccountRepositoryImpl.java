package com.superum.api.v3.account.impl;

import com.superum.api.v3.account.AccountRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static timestar_v2.Tables.ACCOUNT;

@Repository
@Transactional
public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public int create(Integer id, String username, String password, String accountType, long createdAt, long updatedAt) {
        return sql.insertInto(ACCOUNT)
                .set(ACCOUNT.ID, id)
                .set(ACCOUNT.USERNAME, username)
                .set(ACCOUNT.PASSWORD, password)
                .set(ACCOUNT.ACCOUNT_TYPE, accountType)
                .set(ACCOUNT.CREATED_AT, createdAt)
                .set(ACCOUNT.UPDATED_AT, updatedAt)
                .execute();
    }

    // CONSTRUCTORS

    @Autowired
    public AccountRepositoryImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
