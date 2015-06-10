package com.superum.db.teacher.lang;

import org.springframework.stereotype.Service;

@Service
public interface LanguageService {

	Languages addLanguagesToTeacher(Languages languages);
	
	Languages getLanguagesForTeacher(int teacherId);
	
	Languages deleteLanguagesForTeacher(Languages languages);
	
}
