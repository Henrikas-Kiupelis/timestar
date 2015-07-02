package com.superum.db.lesson.attendance.code;

import org.springframework.stereotype.Service;

@Service
public interface CodeService {

	int verifyStudentId(long lessonId, int code);
	
}
