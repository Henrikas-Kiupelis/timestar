package com.superum.db.lesson;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LessonQueriesImpl implements LessonQueries {

	// CONSTRUCTORS

	@Autowired
	public LessonQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
