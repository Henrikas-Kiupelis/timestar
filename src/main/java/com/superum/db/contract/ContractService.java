package com.superum.db.contract;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ContractService {

	Contract addContract(Contract contract);
	
	Contract findContract(int id);
	
	Contract updateContract(Contract contract);
	
	Contract deleteContract(int id);
	
	List<Contract> findContractsForGroup(int groupId);
	
	List<Contract> findContractsForCustomer(int customerId);
	
}
