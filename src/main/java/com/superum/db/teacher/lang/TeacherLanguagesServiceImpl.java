package com.superum.db.teacher.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherLanguagesServiceImpl implements TeacherLanguagesService {

	@Override
	public TeacherLanguages addLanguagesToTeacher(TeacherLanguages languages, int partitionId) {
		LOG.debug("Creating new TeacherLanguages: {}", languages);
		
		TeacherLanguages newLanguages = languageDAO.create(languages, partitionId);
		LOG.debug("New TeacherLanguages created: {}", newLanguages);
		
		return newLanguages;
	}

	@Override
	public TeacherLanguages getLanguagesForTeacher(int teacherId, int partitionId) {
		LOG.debug("Reading TeacherLanguages by ID: {}", teacherId);
		
		TeacherLanguages languages = languageDAO.read(teacherId, partitionId);
		LOG.debug("TeacherLanguages retrieved: {}", languages);
		
		return languages;
	}
	
	@Override
	public TeacherLanguages updateLanguagesForTeacher(TeacherLanguages languages, int partitionId) {
		LOG.debug("Updating TeacherLanguages: {}", languages);
		
		TeacherLanguages oldLanguages = languageDAO.update(languages, partitionId);
		LOG.debug("Old TeacherLanguages retrieved: {}", oldLanguages);
		
		return oldLanguages;
	}
	
	@Override
	public TeacherLanguages deleteLanguagesForTeacher(int teacherId, int partitionId) {
		LOG.debug("Deleting TeacherLanguages by ID: {}", teacherId);
		
		TeacherLanguages deletedLanguages = languageDAO.delete(teacherId, partitionId);
		LOG.debug("Deleted TeacherLanguages: {}", deletedLanguages);
		
		return deletedLanguages;
	}

	@Override
	public TeacherLanguages deleteLanguagesForTeacher(TeacherLanguages languages, int partitionId) {
		LOG.debug("Deleting TeacherLanguages: {}", languages);
		
		TeacherLanguages languagesBeforeDeletion = languageDAO.delete(languages, partitionId);
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
