package com.superum.db.customer.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface GroupDAO extends SimpleDAO<Group, Integer> {

	List<Group> readAllFor(int customerId);
	
}
