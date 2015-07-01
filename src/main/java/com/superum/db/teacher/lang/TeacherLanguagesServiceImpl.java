package com.superum.db.teacher.lang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherLanguagesServiceImpl implements TeacherLanguagesService {

	@Override
	public TeacherLanguages addLanguagesToTeacher(TeacherLanguages languages) {
		return languageDAO.create(languages);
	}

	@Override
	public TeacherLanguages getLanguagesForTeacher(int teacherId) {
		return languageDAO.read(teacherId);
	}
	
	@Override
	public TeacherLanguages updateLanguagesForTeacher(TeacherLanguages languages) {
		return languageDAO.update(languages);
	}
	
	@Override
	public TeacherLanguages deleteLanguagesForTeacher(int teacherId) {
		return languageDAO.delete(teacherId);
	}

	@Override
	public TeacherLanguages deleteLanguagesForTeacher(TeacherLanguages languages) {
		return languageDAO.delete(languages);
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherLanguagesServiceImpl(TeacherLanguagesDAO languageDAO) {
		this.languageDAO = languageDAO;
	}

	// PRIVATE
	
	private final TeacherLanguagesDAO languageDAO;

}
