package com.superum.db.teacher.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherContractServiceImpl implements TeacherContractService {

	@Override
	public TeacherContract addContract(TeacherContract contract) {
		return contractDAO.create(contract);
	}

	@Override
	public TeacherContract findContract(int id) {
		return contractDAO.read(id);
	}

	@Override
	public TeacherContract updateContract(TeacherContract contract) {
		return contractDAO.update(contract);
	}

	@Override
	public TeacherContract deleteContract(int id) {
		return contractDAO.delete(id);
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherContractServiceImpl(TeacherContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	// PRIVATE
	
	private final TeacherContractDAO contractDAO;

}
