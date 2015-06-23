package com.superum.db.customer.group.contract;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GroupContractQueries {

	List<GroupContract> readAllForCustomer(int customerId);

}
