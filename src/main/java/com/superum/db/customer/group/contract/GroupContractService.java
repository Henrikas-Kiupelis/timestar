package com.superum.db.customer.group.contract;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface GroupContractService {

	GroupContract addContract(GroupContract contract);
	
	GroupContract findContract(int id);
	
	GroupContract updateContract(GroupContract contract);
	
	GroupContract deleteContract(int id);
	
	List<GroupContract> findContractsForGroup(int groupId);
	
	List<GroupContract> findContractsForCustomer(int customerId);
	
}
