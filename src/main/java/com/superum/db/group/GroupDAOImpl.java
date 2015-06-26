package com.superum.db.group;

import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;

import java.util.List;

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
		return sql.insertInto(STUDENT_GROUP)
				.set(STUDENT_GROUP.TEACHER_ID, group.getTeacherId())
				.set(STUDENT_GROUP.NAME, group.getName())
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

		Group old = read(id);
		
		sql.update(STUDENT_GROUP)
			.set(STUDENT_GROUP.TEACHER_ID, group.getTeacherId())
			.set(STUDENT_GROUP.NAME, group.getName())
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
	public List<Group> readAllForTeacher(int teacherId) {
		return sql.selectFrom(STUDENT_GROUP)
				.where(STUDENT_GROUP.TEACHER_ID.eq(teacherId))
				.orderBy(STUDENT_GROUP.ID)
				.fetch()
				.map(Group::valueOf);
	}

	// CONSTRUCTORS

	@Autowired
	public GroupDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
