package com.superum.db.lesson;

import com.superum.exception.DatabaseException;
import com.superum.utils.ConditionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;
import static com.superum.db.generated.timestar.Tables.LESSON;

@Repository
@Transactional
public class LessonDAOImpl implements LessonDAO {

	@Override
	public Lesson create(Lesson lesson, int partitionId) {
		try {
            return sql.insertInto(LESSON)
                    .set(LESSON.PARTITION_ID, partitionId)
                    .set(LESSON.GROUP_ID, lesson.getGroupId())
                    .set(LESSON.TIME_OF_START, lesson.getStartTime())
                    .set(LESSON.DURATION_IN_MINUTES, lesson.getLength())
                    .set(LESSON.COMMENT, lesson.getComment())
                    .returning()
                    .fetch().stream()
                    .findFirst()
                    .map(Lesson::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't insert lesson: " + lesson));
		} catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert lesson: " + lesson +
                    "; please refer to the nested exception for more info.", e);
        }
	}

	@Override
	public Lesson read(Long id, int partitionId) {
        try {
            return sql.selectFrom(LESSON)
                    .where(LESSON.ID.eq(id)
                            .and(LESSON.PARTITION_ID.eq(partitionId)))
                    .fetch().stream()
                    .findFirst()
                    .map(Lesson::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't find lesson with ID: " + id));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read lesson for id " + id, e);
        }
	}

	@Override
	public Lesson update(Lesson lesson, int partitionId) {
        try {
            long id = lesson.getId();

            Lesson old = read(id, partitionId);

            sql.update(LESSON)
                    .set(LESSON.GROUP_ID, lesson.getGroupId())
                    .set(LESSON.TIME_OF_START, lesson.getStartTime())
                    .set(LESSON.DURATION_IN_MINUTES, lesson.getLength())
                    .set(LESSON.COMMENT, lesson.getComment())
                    .where(LESSON.ID.eq(id)
                            .and(LESSON.PARTITION_ID.eq(partitionId)))
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update lesson " + lesson, e);
        }
	}

	@Override
	public Lesson delete(Long id, int partitionId) {
        try {
            Lesson old = read(id, partitionId);

            int result = sql.delete(LESSON)
                    .where(LESSON.ID.eq(id)
                            .and(LESSON.PARTITION_ID.eq(partitionId)))
                    .execute();
            if (result == 0)
                throw new DatabaseException("Couldn't delete lesson with ID: " + id);

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete lesson for id " + id, e);
        }
	}

	@Override
	public List<Lesson> readAllForGroup(int groupId, Date start, Date end, int partitionId) {
        try {
            Condition condition = LESSON.GROUP_ID.eq(groupId)
                    .and(LESSON.PARTITION_ID.eq(partitionId));
            Condition dateCondition = ConditionUtils.betweenDates(LESSON.TIME_OF_START, start, end);
            if (dateCondition != null)
                condition = condition.and(dateCondition);

            return sql.selectFrom(LESSON)
                    .where(condition)
                    .orderBy(LESSON.ID)
                    .fetch()
                    .map(Lesson::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read lessons for group with id " + groupId +
                    " between dates " + start + " and " + end, e);
        }
	}

    @Override
    public List<Lesson> readAllForTeacher(int teacherId, Date start, Date end, int partitionId) {
        try {
            Condition condition = LESSON.TEACHER_ID.eq(teacherId)
                    .and(LESSON.PARTITION_ID.eq(partitionId));
            Condition dateCondition = ConditionUtils.betweenDates(LESSON.TIME_OF_START, start, end);
            if (dateCondition != null)
                condition = condition.and(dateCondition);

            return sql.selectFrom(LESSON)
                    .where(condition)
                    .orderBy(LESSON.ID)
                    .fetch()
                    .map(Lesson::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read lessons for teacher with id " + teacherId +
                    " between dates " + start + " and " + end, e);
        }
    }

	@Override
	public boolean isOverlapping(Lesson lesson, int partitionId) {
        try {
            Integer teacherId = lesson.getTeacherId();
            if (teacherId == null)
                teacherId = sql.select(GROUP_OF_STUDENTS.TEACHER_ID)
                        .from(GROUP_OF_STUDENTS)
                        .where(GROUP_OF_STUDENTS.ID.eq(lesson.getGroupId()))
                        .fetch().stream()
                        .findFirst()
                        .map(record -> record.getValue(GROUP_OF_STUDENTS.TEACHER_ID))
                        .orElseThrow(() -> new DatabaseException("Problem retrieving teacher for specified group: " + lesson.getGroupId()));

            long startTime = lesson.getStartTime();
            long endTime = lesson.getEndTime();

            Condition aLessonForSameTeacher = LESSON.TEACHER_ID.eq(teacherId)
                    .and(LESSON.PARTITION_ID.eq(partitionId));

            Condition aLessonStartsBetweenThisLesson = LESSON.TIME_OF_START.between(startTime, endTime);
            Condition aLessonEndsBetweenThisLesson = LESSON.TIME_OF_END.between(startTime, endTime);
            Condition thisLessonStartsBetweenALesson = LESSON.TIME_OF_START.le(startTime).and(LESSON.TIME_OF_END.ge(startTime));
            // No need to check for end time, because it is automatically caught by the first two conditions as well

            Condition lessonIsOverlapping = aLessonForSameTeacher.and(aLessonStartsBetweenThisLesson.or(aLessonEndsBetweenThisLesson).or(thisLessonStartsBetweenALesson));

            if (lesson.hasId())
                lessonIsOverlapping = LESSON.ID.ne(lesson.getId()).and(lessonIsOverlapping);

            return sql.fetchExists(LESSON, lessonIsOverlapping);
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to find out if a lesson overlaps with existing lessons: " + lesson, e);
        }
	}
	
	// CONSTRUCTORS

	@Autowired
	public LessonDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
