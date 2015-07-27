package com.superum.db.group;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.STUDENT_IBFK_1;
import static com.superum.db.generated.timestar.Tables.STUDENT;
import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;

@Repository
@Transactional
public class GroupQueriesImpl implements GroupQueries {

	@Override
	public List<Group> readAllForCustomer(int customerId, int partitionId) {
		try {
            return sql.select(STUDENT_GROUP.fields())
                    .from(STUDENT_GROUP)
                    .join(STUDENT).onKey(STUDENT_IBFK_1)
                    .where(STUDENT.CUSTOMER_ID.eq(customerId)
                            .and(STUDENT_GROUP.PARTITION_ID.eq(partitionId)))
                    .groupBy(STUDENT_GROUP.ID)
                    .orderBy(STUDENT_GROUP.ID)
                    .fetch()
                    .map(Group::valueOf);
		} catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read groups for customer with id " + customerId, e);
        }
	}

	@Override
	public List<Group> readAllForCustomerAndTeacher(int customerId, int teacherId, int partitionId) {
        try {
            return sql.select(STUDENT_GROUP.fields())
                    .from(STUDENT_GROUP)
                    .join(STUDENT).onKey(STUDENT_IBFK_1)
                    .where(STUDENT.CUSTOMER_ID.eq(customerId)
                            .and(STUDENT_GROUP.TEACHER_ID.eq(teacherId))
                            .and(STUDENT_GROUP.PARTITION_ID.eq(partitionId)))
                    .groupBy(STUDENT_GROUP.ID)
                    .orderBy(STUDENT_GROUP.ID)
                    .fetch()
                    .map(Group::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read groups for teacher with id " + teacherId +
                    " and customer with id " + customerId, e);
        }
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public GroupQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
