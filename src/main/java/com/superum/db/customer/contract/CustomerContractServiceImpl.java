package com.superum.db.customer.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerContractServiceImpl implements CustomerContractService {

	@Override
	public CustomerContract addContract(CustomerContract contract) {
		return contractDAO.create(contract);
	}

	@Override
	public CustomerContract findContract(int id) {
		return contractDAO.read(id);
	}

	@Override
	public CustomerContract updateContract(CustomerContract contract) {
		return contractDAO.update(contract);
	}

	@Override
	public CustomerContract deleteContract(int id) {
		return contractDAO.delete(id);
	}

	// CONSTRUCTORS

	@Autowired
	public CustomerContractServiceImpl(CustomerContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	// PRIVATE
	
	private final CustomerContractDAO contractDAO;

}
