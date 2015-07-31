package com.superum.db.customer;

import com.superum.db.generated.timestar.Keys;
import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;

@Repository
@Transactional
public class CustomerQueriesImpl implements CustomerQueries {

	@Override
	public List<Customer> readAllForTeacher(int teacherId, int partitionId) {
		try {
            return sql.select(CUSTOMER.fields())
                    .from(CUSTOMER)
                    .join(GROUP_OF_STUDENTS).onKey(Keys.GROUP_OF_STUDENTS_IBFK_1)
                    .where(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                            .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                    .groupBy(CUSTOMER.ID)
                    .orderBy(CUSTOMER.ID)
                    .fetch()
                    .map(Customer::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers for teacher with id " + teacherId, e);
        }
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
