package com.superum.db.customer.group;

import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class GroupDAOImpl implements GroupDAO {

	@Override
	public Group create(Group group) {
		int customerId = group.getCustomerId();
		String name = group.getName();
		
		return sql.insertInto(STUDENT_GROUP)
				.set(STUDENT_GROUP.CUSTOMER_ID, customerId)
				.set(STUDENT_GROUP.NAME, name)
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Group::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert group: " + group));
	}

	@Override
	public Group read(Integer id) {
		return sql.selectFrom(STUDENT_GROUP)
				.where(STUDENT_GROUP.ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(Group::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find group with ID: " + id));
	}

	@Override
	public Group update(Group group) {
		int id = group.getId();
		int customerId = group.getCustomerId();
		String name = group.getName();
		
		Group old = read(id);
		
		sql.update(STUDENT_GROUP)
			.set(STUDENT_GROUP.CUSTOMER_ID, customerId)
			.set(STUDENT_GROUP.NAME, name)
			.where(STUDENT_GROUP.ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public Group delete(Integer id) {
		Group old = read(id);
		
		int result = sql.delete(STUDENT_GROUP)
				.where(STUDENT_GROUP.ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete group with ID: " + id);
		
		return old;
	}

	@Override
	public List<Group> readAllForCustomer(int customerId) {
		return sql.selectFrom(STUDENT_GROUP)
				.where(STUDENT_GROUP.CUSTOMER_ID.eq(customerId))
				.fetch().stream()
				.map(Group::valueOf)
				.collect(Collectors.toList());
	}

	// CONSTRUCTORS

	@Autowired
	public GroupDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
