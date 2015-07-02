package com.superum.db.lesson.attendance.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {

	@Override
	public int verifyStudentId(long lessonId, int code) {
		return codeDAO.find(lessonId, code);
	}

	// CONSTRUCTORS

	@Autowired
	public CodeServiceImpl(CodeDAO codeDAO) {
		this.codeDAO = codeDAO;
	}

	// PRIVATE
	
	private final CodeDAO codeDAO;

}
