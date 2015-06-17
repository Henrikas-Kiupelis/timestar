package com.superum.db.customer.group;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GroupQueriesImpl implements GroupQueries {

	// CONSTRUCTORS

	@Autowired
	public GroupQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
