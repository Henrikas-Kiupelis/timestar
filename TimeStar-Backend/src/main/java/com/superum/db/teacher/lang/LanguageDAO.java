package com.superum.db.teacher.lang;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface LanguageDAO extends SimpleDAO<Languages, Integer> {
	
	Languages delete(Languages languages);
	
}
