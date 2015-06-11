package com.superum.db.customer.group.student;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface StudentDAO extends SimpleDAO<Student, Integer> {

	List<Student> readAllForCustomer(int customerId);
	
	List<Student> readAllForGroup(int groupId);
	
}
