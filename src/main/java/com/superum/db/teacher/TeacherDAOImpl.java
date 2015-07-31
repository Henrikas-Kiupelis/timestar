package com.superum.db.teacher;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
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
		try {
            return sql.insertInto(TEACHER)
                    .set(TEACHER.PARTITION_ID, partitionId)
                    .set(TEACHER.PAYMENT_DAY, teacher.getPaymentDay())
                    .set(TEACHER.HOURLY_WAGE, teacher.getHourlyWage())
                    .set(TEACHER.ACADEMIC_WAGE, teacher.getAcademicWage())
                    .set(TEACHER.NAME, teacher.getName())
                    .set(TEACHER.SURNAME, teacher.getSurname())
                    .set(TEACHER.PHONE, teacher.getPhone())
                    .set(TEACHER.CITY, teacher.getCity())
                    .set(TEACHER.EMAIL, teacher.getEmail())
                    .set(TEACHER.PICTURE, teacher.getPicture())
                    .set(TEACHER.DOCUMENT, teacher.getDocument())
                    .set(TEACHER.COMMENT, teacher.getComment())
                    .returning()
                    .fetch().stream()
                    .findFirst()
                    .map(Teacher::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't insert teacher: " + teacher));
		} catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert teacher: " + teacher +
                    "; please refer to the nested exception for more info.", e);
        }
	}

	@Override
	public Teacher read(Integer id, int partitionId) {
        try {
            return sql.selectFrom(TEACHER)
                    .where(TEACHER.ID.eq(id)
                            .and(TEACHER.PARTITION_ID.eq(partitionId)))
                    .fetch().stream()
                    .findFirst()
                    .map(Teacher::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't find teacher with ID: " + id));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read teacher for id " + id, e);
        }
	}

	@Override
	public Teacher update(Teacher teacher, int partitionId) {
        try {
            int id = teacher.getId();

            Teacher old = read(id, partitionId);

            sql.update(TEACHER)
                    .set(TEACHER.PAYMENT_DAY, teacher.getPaymentDay())
                    .set(TEACHER.HOURLY_WAGE, teacher.getHourlyWage())
                    .set(TEACHER.ACADEMIC_WAGE, teacher.getAcademicWage())
                    .set(TEACHER.NAME, teacher.getName())
                    .set(TEACHER.SURNAME, teacher.getSurname())
                    .set(TEACHER.PHONE, teacher.getPhone())
                    .set(TEACHER.CITY, teacher.getCity())
                    .set(TEACHER.EMAIL, teacher.getEmail())
                    .set(TEACHER.PICTURE, teacher.getPicture())
                    .set(TEACHER.DOCUMENT, teacher.getDocument())
                    .set(TEACHER.COMMENT, teacher.getComment())
                    .where(TEACHER.ID.eq(id)
                            .and(TEACHER.PARTITION_ID.eq(partitionId)))
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete teacher " + teacher, e);
        }
	}

	@Override
	public Teacher delete(Integer id, int partitionId) {
        try {
            Teacher old = read(id, partitionId);

            int result = sql.delete(TEACHER)
                    .where(TEACHER.ID.eq(id)
                            .and(TEACHER.PARTITION_ID.eq(partitionId)))
                    .execute();
            if (result == 0)
                throw new DatabaseException("Couldn't delete teacher with ID: " + id);

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete teacher for id " + id, e);
        }
	}

	@Override
	public List<Teacher> readAll(int partitionId) {
        try {
            return sql.selectFrom(TEACHER)
                    .where(TEACHER.PARTITION_ID.eq(partitionId))
                    .orderBy(TEACHER.ID)
                    .fetch()
                    .map(Teacher::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all teachers", e);
        }
	}
	
	@Override
	public List<Teacher> readSome(int amount, int offset, int partitionId) {
        try {
            return sql.selectFrom(TEACHER)
                    .where(TEACHER.PARTITION_ID.eq(partitionId))
                    .orderBy(TEACHER.ID)
                    .limit(amount)
                    .offset(offset)
                    .fetch()
                    .map(Teacher::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read " + amount + " teachers, skipping " + offset, e);
        }
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
