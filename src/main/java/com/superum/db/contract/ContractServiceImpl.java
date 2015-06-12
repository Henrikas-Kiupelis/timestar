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
	public Contract findContractForTeacherAndCustomer(int teacherId, int customerId) {
		return contractDAO.readForTeacherAndCustomer(teacherId, customerId);
	}

	@Override
	public List<Contract> findContractsForTeacher(int teacherId) {
		return contractDAO.readAllForTeacher(teacherId);
	}

	@Override
	public List<Contract> findContractsForCustomer(int customerId) {
		return contractDAO.readAllForCustomer(customerId);
	}

	// CONSTRUCTORS

	@Autowired
	public ContractServiceImpl(ContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	// PRIVATE
	
	private final ContractDAO contractDAO;

}
