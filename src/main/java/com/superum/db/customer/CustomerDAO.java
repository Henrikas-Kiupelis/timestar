package com.superum.db.customer;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.FullAccessDAO;
import com.superum.db.dao.SimpleDAO;

@Repository
public interface CustomerDAO extends SimpleDAO<Customer, Integer>, FullAccessDAO<Customer> {}
