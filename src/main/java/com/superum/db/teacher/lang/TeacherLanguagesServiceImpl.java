package com.superum.db.teacher.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherLanguagesServiceImpl implements TeacherLanguagesService {

	@Override
	public TeacherLanguages addLanguagesToTeacher(TeacherLanguages languages) {
		LOG.debug("Creating new TeacherLanguages: {}", languages);
		
		TeacherLanguages newLanguages = languageDAO.create(languages);
		LOG.debug("New TeacherLanguages created: {}", newLanguages);
		
		return newLanguages;
	}

	@Override
	public TeacherLanguages getLanguagesForTeacher(int teacherId) {
		LOG.debug("Reading TeacherLanguages by ID: {}", teacherId);
		
		TeacherLanguages languages = languageDAO.read(teacherId);
		LOG.debug("TeacherLanguages retrieved: {}", languages);
		
		return languages;
	}
	
	@Override
	public TeacherLanguages updateLanguagesForTeacher(TeacherLanguages languages) {
		LOG.debug("Updating TeacherLanguages: {}", languages);
		
		TeacherLanguages oldLanguages = languageDAO.update(languages);
		LOG.debug("Old TeacherLanguages retrieved: {}", oldLanguages);
		
		return oldLanguages;
	}
	
	@Override
	public TeacherLanguages deleteLanguagesForTeacher(int teacherId) {
		LOG.debug("Deleting TeacherLanguages by ID: {}", teacherId);
		
		TeacherLanguages deletedLanguages = languageDAO.delete(teacherId);
		LOG.debug("Deleted TeacherLanguages: {}", deletedLanguages);
		
		return deletedLanguages;
	}

	@Override
	public TeacherLanguages deleteLanguagesForTeacher(TeacherLanguages languages) {
		LOG.debug("Deleting TeacherLanguages: {}", languages);
		
		TeacherLanguages languagesBeforeDeletion = languageDAO.delete(languages);
		LOG.debug("TeacherLanguages before deletion: {}", languagesBeforeDeletion);
		
		return languagesBeforeDeletion;
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherLanguagesServiceImpl(TeacherLanguagesDAO languageDAO) {
		this.languageDAO = languageDAO;
	}

	// PRIVATE
	
	private final TeacherLanguagesDAO languageDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(TeacherLanguagesService.class);

}
