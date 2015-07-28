package com.superum.db.group.student;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.STUDENT;

@Repository
@Transactional
public class StudentDAOImpl implements StudentDAO {

	@Override
	public Student create(Student student, int partitionId) {
		try {
            return sql.insertInto(STUDENT)
                    .set(STUDENT.PARTITION_ID, partitionId)
                    .set(STUDENT.GROUP_ID, student.getGroupId())
                    .set(STUDENT.CUSTOMER_ID, student.getCustomerId())
                    .set(STUDENT.EMAIL, student.getEmail())
                    .set(STUDENT.NAME, student.getName())
                    .returning()
                    .fetch().stream()
                    .findFirst()
                    .map(Student::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't insert student: " + student));
		} catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert student: " + student +
                    "; please refer to the nested exception for more info.", e);
        }
	}

	@Override
	public Student read(Integer id, int partitionId) {
        try {
            return sql.selectFrom(STUDENT)
                    .where(STUDENT.ID.eq(id)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .fetch().stream()
                    .findFirst()
                    .map(Student::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't find student with ID: " + id));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read student for id " + id, e);
        }
	}

	@Override
	public Student update(Student student, int partitionId) {
        try {
            int id = student.getId();

            Student old = read(id, partitionId);

            sql.update(STUDENT)
                    .set(STUDENT.GROUP_ID, student.getGroupId())
                    .set(STUDENT.CUSTOMER_ID, student.getCustomerId())
                    .set(STUDENT.EMAIL, student.getEmail())
                    .set(STUDENT.NAME, student.getName())
                    .where(STUDENT.ID.eq(id)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update student " + student, e);
        }
	}

	@Override
	public Student delete(Integer id, int partitionId) {
        try {
            Student old = read(id, partitionId);

            int result = sql.delete(STUDENT)
                    .where(STUDENT.ID.eq(id)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .execute();
            if (result == 0)
                throw new DatabaseException("Couldn't delete student with ID: " + id);

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update student for id " + id, e);
        }
	}

	@Override
	public List<Student> readAllForGroup(int groupId, int partitionId) {
        try {
            return sql.selectFrom(STUDENT)
                    .where(STUDENT.GROUP_ID.eq(groupId)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .orderBy(STUDENT.ID)
                    .fetch()
                    .map(Student::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all students for group with id " + groupId, e);
        }
	}
	
	@Override
	public List<Student> readAllForCustomer(int customerId, int partitionId) {
        try {
            return sql.selectFrom(STUDENT)
                    .where(STUDENT.CUSTOMER_ID.eq(customerId)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .orderBy(STUDENT.ID)
                    .fetch()
                    .map(Student::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all students for customer with id " + customerId, e);
        }
	}
	
	@Override
	public List<Student> deleteAllForCustomer(int customerId, int partitionId) {
        try {
            List<Student> old = readAllForCustomer(customerId, partitionId);

            sql.delete(STUDENT)
                    .where(STUDENT.CUSTOMER_ID.eq(customerId)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete all students for customer with id " + customerId, e);
        }
	}

	// CONSTRUCTORS

	@Autowired
	public StudentDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
