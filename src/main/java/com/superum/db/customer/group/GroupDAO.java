package com.superum.db.customer.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface GroupDAO extends SimpleDAO<Group, Integer> {

	List<Group> readAllForCustomer(int customerId);
	
	List<Group> readAllForTeacher(int teacherId);
	
	List<Group> readAllForCustomerAndTeacher(int customerId, int teacherId);
	
}
