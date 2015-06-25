package com.superum.db.customer;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomerQueries {

	List<Customer> readAllForTeacher(int teacherId);
	
	List<Customer> readAllForLessons(Date start, Date end);
	
}
