package com.superum.db.teacher.lang;

import org.springframework.stereotype.Service;

@Service
public interface LanguagesService {

	Languages addLanguagesToTeacher(Languages languages);
	
	Languages getLanguagesForTeacher(int teacherId);
	
	Languages updateLanguagesForTeacher(Languages languages);
	
	Languages deleteLanguagesForTeacher(int teacherId);
	
	Languages deleteLanguagesForTeacher(Languages languages);
	
}
