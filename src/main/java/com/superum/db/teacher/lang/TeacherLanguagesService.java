package com.superum.db.teacher.lang;

import org.springframework.stereotype.Service;

@Service
public interface TeacherLanguagesService {

	TeacherLanguages addLanguagesToTeacher(TeacherLanguages languages, int partitionId);
	
	TeacherLanguages getLanguagesForTeacher(int teacherId, int partitionId);
	
	TeacherLanguages updateLanguagesForTeacher(TeacherLanguages languages, int partitionId);
	
	TeacherLanguages deleteLanguagesForTeacher(int teacherId, int partitionId);
	
	TeacherLanguages deleteLanguagesForTeacher(TeacherLanguages languages, int partitionId);
	
}
