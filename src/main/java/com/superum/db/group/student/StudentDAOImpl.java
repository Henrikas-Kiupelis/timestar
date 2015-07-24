package com.superum.db.group.student;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
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
	}

	@Override
	public Student read(Integer id, int partitionId) {
		return sql.selectFrom(STUDENT)
				.where(STUDENT.ID.eq(id)
						.and(STUDENT.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(Student::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find student with ID: " + id));
	}

	@Override
	public Student update(Student student, int partitionId) {
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
	}

	@Override
	public Student delete(Integer id, int partitionId) {
		Student old = read(id, partitionId);
		
		int result = sql.delete(STUDENT)
				.where(STUDENT.ID.eq(id)
						.and(STUDENT.PARTITION_ID.eq(partitionId)))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete student with ID: " + id);
		
		return old;
	}

	@Override
	public List<Student> readAllForGroup(int groupId, int partitionId) {
		return sql.selectFrom(STUDENT)
				.where(STUDENT.GROUP_ID.eq(groupId)
						.and(STUDENT.PARTITION_ID.eq(partitionId)))
				.orderBy(STUDENT.ID)
				.fetch()
				.map(Student::valueOf);
	}
	
	@Override
	public List<Student> readAllForCustomer(int customerId, int partitionId) {
		return sql.selectFrom(STUDENT)
				.where(STUDENT.CUSTOMER_ID.eq(customerId)
						.and(STUDENT.PARTITION_ID.eq(partitionId)))
				.orderBy(STUDENT.ID)
				.fetch()
				.map(Student::valueOf);
	}
	
	@Override
	public List<Student> deleteAllForCustomer(int customerId, int partitionId) {
		List<Student> old = readAllForCustomer(customerId, partitionId);
		
		sql.delete(STUDENT)
			.where(STUDENT.CUSTOMER_ID.eq(customerId)
					.and(STUDENT.PARTITION_ID.eq(partitionId)))
			.execute();
		
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public StudentDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
