package com.superum.db.customer;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomerQueries {

	List<Customer> readAllForTeacher(int teacherId);
	
}
