package com.superum.db.customer.contract;

import org.springframework.stereotype.Service;

@Service
public interface CustomerContractService {

	CustomerContract addContract(CustomerContract contract);
	
	CustomerContract findContract(int id);
	
	CustomerContract updateContract(CustomerContract contract);
	
	CustomerContract deleteContract(int id);
	
}
