package com.superum.db.teacher;

import static com.superum.db.generated.timestar.Tables.TEACHER;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class TeacherDAOImpl implements TeacherDAO {

	@Override
	public Teacher create(Teacher teacher) {
		String name = teacher.getName();
		String surname = teacher.getSurname();
		String phone = teacher.getPhone();
		String comment = teacher.getComment();

		return sql.insertInto(TEACHER)
				.set(TEACHER.NAME, name)
				.set(TEACHER.SURNAME, surname)
				.set(TEACHER.PHONE, phone)
				.set(TEACHER.COMMENT_ABOUT, comment)
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Teacher::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert teacher: " + teacher));
	}

	@Override
	public Teacher read(Integer id) {
		return sql.selectFrom(TEACHER)
				.where(TEACHER.ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(Teacher::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find teacher with ID: " + id));
	}

	@Override
	public Teacher update(Teacher teacher) {
		int id = teacher.getId();
		String name = teacher.getName();
		String surname = teacher.getSurname();
		String phone = teacher.getPhone();
		String comment = teacher.getComment();
		
		Teacher old = read(id);
		
		sql.update(TEACHER)
			.set(TEACHER.NAME, name)
			.set(TEACHER.SURNAME, surname)
			.set(TEACHER.PHONE, phone)
			.set(TEACHER.COMMENT_ABOUT, comment)
			.where(TEACHER.ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public Teacher delete(Integer id) {
		Teacher old = read(id);
		
		int result = sql.delete(TEACHER)
				.where(TEACHER.ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete teacher with ID: " + id);
		
		return old;
	}

	// CONSTRUCTORS
	
	@Autowired
	public TeacherDAOImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
