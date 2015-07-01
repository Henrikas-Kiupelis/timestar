package com.superum.db.teacher.lang;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface TeacherLanguagesDAO extends SimpleDAO<TeacherLanguages, Integer> {
	
	TeacherLanguages delete(TeacherLanguages languages);
	
}
