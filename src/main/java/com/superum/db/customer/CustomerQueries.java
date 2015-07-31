package com.superum.db.customer;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerQueries {

	List<Customer> readAllForTeacher(int teacherId, int partitionId);
	
}
