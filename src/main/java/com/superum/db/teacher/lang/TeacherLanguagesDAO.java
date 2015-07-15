package com.superum.db.teacher.lang;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface TeacherLanguagesDAO extends SimplePartitionedDAO<TeacherLanguages, Integer> {
	
	TeacherLanguages delete(TeacherLanguages languages, int partitionId);
	
}
