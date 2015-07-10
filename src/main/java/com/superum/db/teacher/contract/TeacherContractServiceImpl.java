package com.superum.db.teacher.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherContractServiceImpl implements TeacherContractService {

	@Override
	public TeacherContract addContract(TeacherContract contract) {
		LOG.debug("Creating new TeacherContract: {}", contract);
		
		TeacherContract newContract = contractDAO.create(contract);
		LOG.debug("New TeacherContract created: {}", newContract);
		
		return newContract;
	}

	@Override
	public TeacherContract findContract(int id) {
		LOG.debug("Reading TeacherContract by ID: {}", id);
		
		TeacherContract contract = contractDAO.read(id);
		LOG.debug("TeacherContract retrieved: {}", contract);
		
		return contract;
	}

	@Override
	public TeacherContract updateContract(TeacherContract contract) {
		LOG.debug("Updating TeacherContract: {}", contract);
		
		TeacherContract oldContract = contractDAO.update(contract);
		LOG.debug("Old TeacherContract retrieved: {}", oldContract);
		
		return oldContract;
	}

	@Override
	public TeacherContract deleteContract(int id) {
		LOG.debug("Deleting TeacherContract by ID: {}", id);
		
		TeacherContract deletedContract = contractDAO.delete(id);
		LOG.debug("TeacherContract deleted: {}", deletedContract);
		
		return deletedContract;
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherContractServiceImpl(TeacherContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	// PRIVATE
	
	private final TeacherContractDAO contractDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(TeacherContractService.class);

}
