package com.superum.db.group;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;

@Repository
@Transactional
public class GroupDAOImpl implements GroupDAO {

	@Override
	public Group create(Group group, int partitionId) {
		try {
            return sql.insertInto(GROUP_OF_STUDENTS)
                    .set(GROUP_OF_STUDENTS.PARTITION_ID, partitionId)
                    .set(GROUP_OF_STUDENTS.CUSTOMER_ID, group.getCustomerId())
                    .set(GROUP_OF_STUDENTS.TEACHER_ID, group.getTeacherId())
                    .set(GROUP_OF_STUDENTS.USE_HOURLY_WAGE, group.getUsesHourlyWage())
                    .set(GROUP_OF_STUDENTS.LANGUAGE_LEVEL, group.getLanguageLevel())
                    .set(GROUP_OF_STUDENTS.NAME, group.getName())
                    .returning()
                    .fetch().stream()
                    .findFirst()
                    .map(Group::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't insert group: " + group));
        } catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert group: " + group +
                    "; please refer to the nested exception for more info.", e);
        }
	}

	@Override
	public Group read(Integer id, int partitionId) {
        try {
            return sql.selectFrom(GROUP_OF_STUDENTS)
                    .where(GROUP_OF_STUDENTS.ID.eq(id)
                            .and(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId)))
                    .fetch().stream()
                    .findFirst()
                    .map(Group::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't find group with ID: " + id));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read group for id " + id, e);
        }
	}

	@Override
	public Group update(Group group, int partitionId) {
        try {
            int id = group.getId();

            Group old = read(id, partitionId);

            sql.update(GROUP_OF_STUDENTS)
                    .set(GROUP_OF_STUDENTS.CUSTOMER_ID, group.getCustomerId())
                    .set(GROUP_OF_STUDENTS.TEACHER_ID, group.getTeacherId())
                    .set(GROUP_OF_STUDENTS.USE_HOURLY_WAGE, group.getUsesHourlyWage())
                    .set(GROUP_OF_STUDENTS.LANGUAGE_LEVEL, group.getLanguageLevel())
                    .set(GROUP_OF_STUDENTS.NAME, group.getName())
                    .where(GROUP_OF_STUDENTS.ID.eq(id)
                            .and(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId)))
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update group " + group, e);
        }
	}

	@Override
	public Group delete(Integer id, int partitionId) {
        try {
            Group old = read(id, partitionId);

            int result = sql.delete(GROUP_OF_STUDENTS)
                    .where(GROUP_OF_STUDENTS.ID.eq(id)
                            .and(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId)))
                    .execute();
            if (result == 0)
                throw new DatabaseException("Couldn't delete group with ID: " + id);

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete group for id " + id, e);
        }
	}
	
	@Override
	public List<Group> readAllForTeacher(int teacherId, int partitionId) {
        try {
            return sql.selectFrom(GROUP_OF_STUDENTS)
                    .where(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                            .and(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId)))
                    .orderBy(GROUP_OF_STUDENTS.ID)
                    .fetch()
                    .map(Group::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read groups for teacher with id " + teacherId, e);
        }
	}

    @Override
    public List<Group> readAllForCustomer(int customerId, int partitionId) {
        try {
            return sql.selectFrom(GROUP_OF_STUDENTS)
                    .where(GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customerId)
                            .and(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId)))
                    .orderBy(GROUP_OF_STUDENTS.ID)
                    .fetch()
                    .map(Group::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read groups for customer with id " + customerId, e);
        }
    }

    @Override
    public List<Group> readAllForCustomerAndTeacher(int customerId, int teacherId, int partitionId) {
        try {
            return sql.selectFrom(GROUP_OF_STUDENTS)
                    .where(GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customerId)
                            .and(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId))
                            .and(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId)))
                    .orderBy(GROUP_OF_STUDENTS.ID)
                    .fetch()
                    .map(Group::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read groups for teacher with id " + teacherId +
                    " and customer with id " + customerId, e);
        }
    }

	@Override
	public List<Group> all(int partitionId) {
        try {
            return sql.selectFrom(GROUP_OF_STUDENTS)
                    .where(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId))
                    .orderBy(GROUP_OF_STUDENTS.ID)
                    .fetch()
                    .map(Group::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all groups", e);
        }
	}

	// CONSTRUCTORS

	@Autowired
	public GroupDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
