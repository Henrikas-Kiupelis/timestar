package com.superum.db.customer.group.contract;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupContractServiceImpl implements GroupContractService {

	@Override
	public GroupContract addContract(GroupContract contract) {
		return contractDAO.create(contract);
	}

	@Override
	public GroupContract findContract(int id) {
		return contractDAO.read(id);
	}

	@Override
	public GroupContract updateContract(GroupContract contract) {
		return contractDAO.update(contract);
	}

	@Override
	public GroupContract deleteContract(int id) {
		return contractDAO.delete(id);
	}

	@Override
	public List<GroupContract> findContractsForGroup(int groupId) {
		return contractDAO.readAllForGroup(groupId);
	}

	@Override
	public List<GroupContract> findContractsForCustomer(int customerId) {
		return contractQueries.readAllForCustomer(customerId);
	}

	// CONSTRUCTORS

	@Autowired
	public GroupContractServiceImpl(GroupContractDAO contractDAO, GroupContractQueries contractQueries) {
		this.contractDAO = contractDAO;
		this.contractQueries = contractQueries;
	}

	// PRIVATE
	
	private final GroupContractDAO contractDAO;
	private final GroupContractQueries contractQueries;

}
