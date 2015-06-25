package com.superum.db.customer.contract;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface CustomerContractDAO extends SimpleDAO<CustomerContract, Integer> {}
