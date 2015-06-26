package com.superum.db.lesson;

import static com.superum.db.generated.timestar.Tables.LESSON;

import java.sql.Date;
import java.util.List;

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
		return sql.insertInto(LESSON)
				.set(LESSON.TEACHER_ID, lesson.getTeacherId())
				.set(LESSON.GROUP_ID, lesson.getGroupId())
				.set(LESSON.DATE_OF_LESSON, lesson.getDate())
				.set(LESSON.TIME_OF_LESSON, lesson.getTime())
				.set(LESSON.LENGTH_IN_MINUTES, lesson.getLength())
				.set(LESSON.COMMENT_ABOUT, lesson.getComment())
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
		
		Lesson old = read(id);
		
		sql.update(LESSON)
			.set(LESSON.TEACHER_ID, lesson.getTeacherId())
			.set(LESSON.GROUP_ID, lesson.getGroupId())
			.set(LESSON.DATE_OF_LESSON, lesson.getDate())
			.set(LESSON.TIME_OF_LESSON, lesson.getTime())
			.set(LESSON.LENGTH_IN_MINUTES, lesson.getLength())
			.set(LESSON.COMMENT_ABOUT, lesson.getComment())
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
	public List<Lesson> readAllForTeacher(int teacherId, Date start, Date end) {
		Condition condition = LESSON.TEACHER_ID.eq(teacherId);
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		return sql.selectFrom(LESSON)
				.where(condition)
				.orderBy(LESSON.ID)
				.fetch()
				.map(Lesson::valueOf);
	}
	
	@Override
	public List<Lesson> readAllForGroup(int groupId, Date start, Date end) {
		Condition condition = LESSON.GROUP_ID.eq(groupId);
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		return sql.selectFrom(LESSON)
				.where(condition)
				.orderBy(LESSON.ID)
				.fetch()
				.map(Lesson::valueOf);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
