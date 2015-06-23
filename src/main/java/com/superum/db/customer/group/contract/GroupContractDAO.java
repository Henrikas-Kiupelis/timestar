package com.superum.db.customer.group.contract;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface GroupContractDAO extends SimpleDAO<GroupContract, Integer> {
	
	List<GroupContract> readAllForGroup(int groupId);
	
}
