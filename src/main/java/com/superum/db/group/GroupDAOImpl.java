package com.superum.db.group;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;

@Repository
@Transactional
public class GroupDAOImpl implements GroupDAO {

	@Override
	public Group create(Group group, int partitionId) {
		return sql.insertInto(STUDENT_GROUP)
				.set(STUDENT_GROUP.PARTITION_ID, partitionId)
				.set(STUDENT_GROUP.TEACHER_ID, group.getTeacherId())
				.set(STUDENT_GROUP.NAME, group.getName())
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Group::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert group: " + group));
	}

	@Override
	public Group read(Integer id, int partitionId) {
		return sql.selectFrom(STUDENT_GROUP)
				.where(STUDENT_GROUP.ID.eq(id)
						.and(STUDENT_GROUP.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(Group::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find group with ID: " + id));
	}

	@Override
	public Group update(Group group, int partitionId) {
		int id = group.getId();

		Group old = read(id, partitionId);
		
		sql.update(STUDENT_GROUP)
			.set(STUDENT_GROUP.TEACHER_ID, group.getTeacherId())
			.set(STUDENT_GROUP.NAME, group.getName())
			.where(STUDENT_GROUP.ID.eq(id)
					.and(STUDENT_GROUP.PARTITION_ID.eq(partitionId)))
			.execute();
		
		return old;
	}

	@Override
	public Group delete(Integer id, int partitionId) {
		Group old = read(id, partitionId);
		
		int result = sql.delete(STUDENT_GROUP)
				.where(STUDENT_GROUP.ID.eq(id)
						.and(STUDENT_GROUP.PARTITION_ID.eq(partitionId)))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete group with ID: " + id);
		
		return old;
	}
	
	@Override
	public List<Group> readAllForTeacher(int teacherId, int partitionId) {
		return sql.selectFrom(STUDENT_GROUP)
				.where(STUDENT_GROUP.TEACHER_ID.eq(teacherId)
						.and(STUDENT_GROUP.PARTITION_ID.eq(partitionId)))
				.orderBy(STUDENT_GROUP.ID)
				.fetch()
				.map(Group::valueOf);
	}

	@Override
	public List<Group> all(int partitionId) {
		return sql.selectFrom(STUDENT_GROUP)
                .where(STUDENT_GROUP.PARTITION_ID.eq(partitionId))
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
