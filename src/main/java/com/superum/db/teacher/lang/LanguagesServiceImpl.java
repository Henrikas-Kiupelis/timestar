package com.superum.db.teacher.lang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguagesServiceImpl implements LanguagesService {

	@Override
	public Languages addLanguagesToTeacher(Languages languages) {
		return languageDAO.create(languages);
	}

	@Override
	public Languages getLanguagesForTeacher(int teacherId) {
		return languageDAO.read(teacherId);
	}
	
	@Override
	public Languages updateLanguagesForTeacher(Languages languages) {
		return languageDAO.update(languages);
	}
	
	@Override
	public Languages deleteLanguagesForTeacher(int teacherId) {
		return languageDAO.delete(teacherId);
	}

	@Override
	public Languages deleteLanguagesForTeacher(Languages languages) {
		return languageDAO.delete(languages);
	}

	// CONSTRUCTORS

	@Autowired
	public LanguagesServiceImpl(LanguagesDAO languageDAO) {
		this.languageDAO = languageDAO;
	}

	// PRIVATE
	
	private final LanguagesDAO languageDAO;

}
