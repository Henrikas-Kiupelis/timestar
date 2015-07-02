package com.superum.db.account;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface AccountDAO extends SimpleDAO<Account, String> {}
