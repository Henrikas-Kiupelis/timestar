package com.superum.db.lesson;

import static com.superum.db.generated.timestar.Tables.LESSON;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;
import com.superum.utils.ConditionUtils;

@Repository
@Transactional
public class LessonDAOImpl implements LessonDAO {

	@Override
	public Lesson create(Lesson lesson) {
		int teacherId = lesson.getTeacherId();
		int customerId = lesson.getCustomerId();
		int groupId = lesson.getGroupId();
		Date date = lesson.getDate();
		short time = lesson.getTime();
		short length = lesson.getLength();
		String comment = lesson.getComment();
		
		return sql.insertInto(LESSON)
				.set(LESSON.TEACHER_ID, teacherId)
				.set(LESSON.CUSTOMER_ID, customerId)
				.set(LESSON.GROUP_ID, groupId)
				.set(LESSON.DATE_OF_LESSON, date)
				.set(LESSON.TIME_OF_LESSON, time)
				.set(LESSON.LENGTH_IN_MINUTES, length)
				.set(LESSON.COMMENT_ABOUT, comment)
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Lesson::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert lesson: " + lesson));
	}

	@Override
	public Lesson read(Long id) {
		return sql.selectFrom(LESSON)
				.where(LESSON.ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(Lesson::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find lesson with ID: " + id));
	}

	@Override
	public Lesson update(Lesson lesson) {
		long id = lesson.getId();
		int teacherId = lesson.getTeacherId();
		int customerId = lesson.getCustomerId();
		int groupId = lesson.getGroupId();
		Date date = lesson.getDate();
		short time = lesson.getTime();
		short length = lesson.getLength();
		String comment = lesson.getComment();
		
		Lesson old = read(id);
		
		sql.update(LESSON)
			.set(LESSON.TEACHER_ID, teacherId)
			.set(LESSON.CUSTOMER_ID, customerId)
			.set(LESSON.GROUP_ID, groupId)
			.set(LESSON.DATE_OF_LESSON, date)
			.set(LESSON.TIME_OF_LESSON, time)
			.set(LESSON.LENGTH_IN_MINUTES, length)
			.set(LESSON.COMMENT_ABOUT, comment)
			.where(LESSON.ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public Lesson delete(Long id) {
		Lesson old = read(id);
		
		int result = sql.delete(LESSON)
				.where(LESSON.ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete lesson with ID: " + id);
		
		return old;
	}

	@Override
	public List<Lesson> readAllFrom(TableBinder table, int id, Date start, Date end) {
		Condition condition = table.field().eq(id);
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		return sql.selectFrom(LESSON)
				.where(condition)
				.fetch().stream()
				.map(Lesson::valueOf)
				.collect(Collectors.toList());
	}

	// CONSTRUCTORS

	@Autowired
	public LessonDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
