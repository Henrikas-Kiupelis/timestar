package com.superum.db.customer.lang;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface CustomerLanguagesDAO extends SimplePartitionedDAO<CustomerLanguages, Integer> {

	CustomerLanguages delete(CustomerLanguages languages, int partitionId);
	
}
