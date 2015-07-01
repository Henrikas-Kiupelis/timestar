package com.superum.db.teacher.lang;

import org.springframework.stereotype.Service;

@Service
public interface TeacherLanguagesService {

	TeacherLanguages addLanguagesToTeacher(TeacherLanguages languages);
	
	TeacherLanguages getLanguagesForTeacher(int teacherId);
	
	TeacherLanguages updateLanguagesForTeacher(TeacherLanguages languages);
	
	TeacherLanguages deleteLanguagesForTeacher(int teacherId);
	
	TeacherLanguages deleteLanguagesForTeacher(TeacherLanguages languages);
	
}
