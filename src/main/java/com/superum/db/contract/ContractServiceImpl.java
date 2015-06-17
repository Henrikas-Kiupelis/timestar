package com.superum.db.contract;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractServiceImpl implements ContractService {

	@Override
	public Contract addContract(Contract contract) {
		return contractDAO.create(contract);
	}

	@Override
	public Contract findContract(int id) {
		return contractDAO.read(id);
	}

	@Override
	public Contract updateContract(Contract contract) {
		return contractDAO.update(contract);
	}

	@Override
	public Contract deleteContract(int id) {
		return contractDAO.delete(id);
	}

	@Override
	public List<Contract> findContractsForGroup(int groupId) {
		return contractDAO.readAllForGroup(groupId);
	}

	@Override
	public List<Contract> findContractsForCustomer(int customerId) {
		return contractQueries.readAllForCustomer(customerId);
	}

	// CONSTRUCTORS

	@Autowired
	public ContractServiceImpl(ContractDAO contractDAO, ContractQueries contractQueries) {
		this.contractDAO = contractDAO;
		this.contractQueries = contractQueries;
	}

	// PRIVATE
	
	private final ContractDAO contractDAO;
	private final ContractQueries contractQueries;

}
