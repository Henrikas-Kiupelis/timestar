package com.superum.db.customer;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.PartitionedAccessDAO;
import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface CustomerDAO extends SimplePartitionedDAO<Customer, Integer>, PartitionedAccessDAO<Customer> {}
