package com.superum.db.teacher;

import static com.superum.db.generated.timestar.Tables.TEACHER;

import java.util.List;

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
		return sql.insertInto(TEACHER)
				.set(TEACHER.NAME, teacher.getName())
				.set(TEACHER.SURNAME, teacher.getSurname())
				.set(TEACHER.PHONE, teacher.getPhone())
				.set(TEACHER.CITY, teacher.getCity())
				.set(TEACHER.EMAIL, teacher.getEmail())
				.set(TEACHER.COMMENT_ABOUT, teacher.getComment())
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
		
		Teacher old = read(id);
		
		sql.update(TEACHER)
			.set(TEACHER.NAME, teacher.getName())
			.set(TEACHER.SURNAME, teacher.getSurname())
			.set(TEACHER.PHONE, teacher.getPhone())
			.set(TEACHER.CITY, teacher.getCity())
			.set(TEACHER.EMAIL, teacher.getEmail())
			.set(TEACHER.COMMENT_ABOUT, teacher.getComment())
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

	@Override
	public List<Teacher> readAll() {
		return sql.selectFrom(TEACHER)
				.orderBy(TEACHER.ID)
				.fetch()
				.map(Teacher::valueOf);
	}
	
	@Override
	public List<Teacher> readSome(int amount, int offset) {
		return sql.selectFrom(TEACHER)
				.orderBy(TEACHER.ID)
				.limit(amount)
				.offset(offset)
				.fetch()
				.map(Teacher::valueOf);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public TeacherDAOImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
