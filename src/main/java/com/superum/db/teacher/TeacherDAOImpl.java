package com.superum.db.teacher;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.TEACHER;

@Repository
@Transactional
public class TeacherDAOImpl implements TeacherDAO {

	@Override
	public Teacher create(Teacher teacher, int partitionId) {
		return sql.insertInto(TEACHER)
				.set(TEACHER.PARTITION_ID, partitionId)
				.set(TEACHER.PAYMENT_DAY, teacher.getPaymentDay())
				.set(TEACHER.NAME, teacher.getName())
				.set(TEACHER.SURNAME, teacher.getSurname())
				.set(TEACHER.PHONE, teacher.getPhone())
				.set(TEACHER.CITY, teacher.getCity())
				.set(TEACHER.EMAIL, teacher.getEmail())
				.set(TEACHER.PICTURE_NAME, teacher.getPictureName())
				.set(TEACHER.DOCUMENT_NAME, teacher.getDocumentName())
				.set(TEACHER.COMMENT_ABOUT, teacher.getComment())
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Teacher::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert teacher: " + teacher));
	}

	@Override
	public Teacher read(Integer id, int partitionId) {
		return sql.selectFrom(TEACHER)
				.where(TEACHER.ID.eq(id)
						.and(TEACHER.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(Teacher::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find teacher with ID: " + id));
	}

	@Override
	public Teacher update(Teacher teacher, int partitionId) {
		int id = teacher.getId();
		
		Teacher old = read(id, partitionId);
		
		sql.update(TEACHER)
			.set(TEACHER.PAYMENT_DAY, teacher.getPaymentDay())
			.set(TEACHER.NAME, teacher.getName())
			.set(TEACHER.SURNAME, teacher.getSurname())
			.set(TEACHER.PHONE, teacher.getPhone())
			.set(TEACHER.CITY, teacher.getCity())
			.set(TEACHER.EMAIL, teacher.getEmail())
			.set(TEACHER.PICTURE_NAME, teacher.getPictureName())
			.set(TEACHER.DOCUMENT_NAME, teacher.getDocumentName())
			.set(TEACHER.COMMENT_ABOUT, teacher.getComment())
			.where(TEACHER.ID.eq(id)
					.and(TEACHER.PARTITION_ID.eq(partitionId)))
			.execute();
		
		return old;
	}

	@Override
	public Teacher delete(Integer id, int partitionId) {
		Teacher old = read(id, partitionId);
		
		int result = sql.delete(TEACHER)
				.where(TEACHER.ID.eq(id)
						.and(TEACHER.PARTITION_ID.eq(partitionId)))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete teacher with ID: " + id);
		
		return old;
	}

	@Override
	public List<Teacher> readAll(int partitionId) {
		return sql.selectFrom(TEACHER)
				.where(TEACHER.PARTITION_ID.eq(partitionId))
				.orderBy(TEACHER.ID)
				.fetch()
				.map(Teacher::valueOf);
	}
	
	@Override
	public List<Teacher> readSome(int amount, int offset, int partitionId) {
		return sql.selectFrom(TEACHER)
				.where(TEACHER.PARTITION_ID.eq(partitionId))
				.orderBy(TEACHER.ID)
				.limit(amount)
				.offset(offset)
				.fetch()
				.map(Teacher::valueOf);
	}
	
	@Override
	public int count(int partitionId) {
		return sql.fetchCount(TEACHER);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public TeacherDAOImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
