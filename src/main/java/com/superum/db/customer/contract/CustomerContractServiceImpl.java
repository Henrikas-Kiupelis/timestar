package com.superum.db.customer.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerContractServiceImpl implements CustomerContractService {

	@Override
	public CustomerContract addContract(CustomerContract contract) {
		LOG.debug("Creating new CustomerContract: {}", contract);
		
		CustomerContract newContract = contractDAO.create(contract);
		LOG.debug("CustomerContract created: {}", newContract);
		
		return newContract;
	}

	@Override
	public CustomerContract findContract(int customerId) {
		LOG.debug("Reading CustomerContract for ID: {}", customerId);
		
		CustomerContract contract = contractDAO.read(customerId);
		LOG.debug("CustomerContract retrieved: {}", contract);
		
		return contract;
	}

	@Override
	public CustomerContract updateContract(CustomerContract contract) {
		LOG.debug("Updating CustomerContract: {}", contract);
		
		CustomerContract oldContract = contractDAO.update(contract);
		LOG.debug("Old CustomerContract retrieved: {}", oldContract);
		
		return oldContract;
	}

	@Override
	public CustomerContract deleteContract(int customerId) {
		LOG.debug("Deleting CustomerContract by ID: {}", customerId);
		
		CustomerContract deletedContract = contractDAO.delete(customerId);
		LOG.debug("Deleted CustomerContract: {}", deletedContract);
		
		return deletedContract;
	}

	// CONSTRUCTORS

	@Autowired
	public CustomerContractServiceImpl(CustomerContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	// PRIVATE
	
	private final CustomerContractDAO contractDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerContractService.class);

}
