package com.superum.db.group.student;

import static com.superum.db.generated.timestar.Tables.STUDENT;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class StudentDAOImpl implements StudentDAO {

	@Override
	public Student create(Student student) {
		return sql.insertInto(STUDENT)
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
	public Student read(Integer id) {
		return sql.selectFrom(STUDENT)
				.where(STUDENT.ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(Student::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find student with ID: " + id));
	}

	@Override
	public Student update(Student student) {
		int id = student.getId();

		Student old = read(id);
		
		sql.update(STUDENT)
			.set(STUDENT.GROUP_ID, student.getGroupId())
			.set(STUDENT.CUSTOMER_ID, student.getCustomerId())
			.set(STUDENT.EMAIL, student.getEmail())
			.set(STUDENT.NAME, student.getName())
			.where(STUDENT.ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public Student delete(Integer id) {
		Student old = read(id);
		
		int result = sql.delete(STUDENT)
				.where(STUDENT.ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete student with ID: " + id);
		
		return old;
	}

	@Override
	public List<Student> readAllForGroup(int groupId) {
		return sql.selectFrom(STUDENT)
				.where(STUDENT.GROUP_ID.eq(groupId))
				.orderBy(STUDENT.ID)
				.fetch()
				.map(Student::valueOf);
	}
	
	@Override
	public List<Student> readAllForCustomer(int customerId) {
		return sql.selectFrom(STUDENT)
				.where(STUDENT.CUSTOMER_ID.eq(customerId))
				.orderBy(STUDENT.ID)
				.fetch()
				.map(Student::valueOf);
	}

	// CONSTRUCTORS

	@Autowired
	public StudentDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
