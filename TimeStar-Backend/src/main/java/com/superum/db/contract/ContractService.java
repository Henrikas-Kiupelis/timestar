package com.superum.db.contract;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ContractService {

	Contract addContract(Contract contract);
	
	Contract findContract(int id);
	
	Contract updateContract(Contract contract);
	
	Contract deleteContract(int id);
	
	Contract findContractForTeacherAndCustomer(int teacherId, int customerId);
	
	List<Contract> findContractsForTeacher(int teacherId);
	
	List<Contract> findContractsForCustomer(int customerId);
	
}
