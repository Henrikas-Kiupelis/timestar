package com.superum.db.teacher.lang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {

	@Override
	public Languages addLanguagesToTeacher(Languages languages) {
		return languageDAO.create(languages);
	}

	@Override
	public Languages getLanguagesForTeacher(int teacherId) {
		return languageDAO.read(teacherId);
	}

	@Override
	public Languages deleteLanguagesForTeacher(Languages languages) {
		return languageDAO.delete(languages);
	}

	// CONSTRUCTORS

	@Autowired
	public LanguageServiceImpl(LanguageDAO languageDAO) {
		this.languageDAO = languageDAO;
	}

	// PRIVATE
	
	private final LanguageDAO languageDAO;

}
