package com.superum.db.contract;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ContractQueries {

	List<Contract> readAllForCustomer(int customerId);

}
