package com.superum.db.teacher.contract;

import org.springframework.stereotype.Service;

@Service
public interface TeacherContractService {

	TeacherContract addContract(TeacherContract contract);
	
	TeacherContract findContract(int id);
	
	TeacherContract updateContract(TeacherContract contract);
	
	TeacherContract deleteContract(int id);
	
}
