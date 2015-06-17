package com.superum.db.contract;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface ContractDAO extends SimpleDAO<Contract, Integer> {
	
	List<Contract> readAllForGroup(int groupId);
	
}
