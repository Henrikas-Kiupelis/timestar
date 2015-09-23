package com.superum.api.v3.lesson;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Function;

import static org.jooq.impl.DSL.trueCondition;
import static timestar_v2.Tables.LESSON;

@Repository
public class LessonRepositoryImpl implements LessonRepository {

    @Override
    public Optional<FetchedLesson> insert(int groupId, int teacherId, long startTime, long endTime, int length,
                                          String comment, int partitionId, Function<Record, FetchedLesson> mapper) {
        return sql.insertInto(LESSON)
                .set(LESSON.GROUP_ID, groupId)
                .set(LESSON.TEACHER_ID, teacherId)
                .set(LESSON.TIME_OF_START, startTime)
                .set(LESSON.TIME_OF_END, endTime)
                .set(LESSON.DURATION_IN_MINUTES, length)
                .set(LESSON.COMMENT, comment)
                .set(LESSON.PARTITION_ID, partitionId)
                .returning()
                .fetch().stream().findAny()
                .map(mapper);
    }

    @Override
    public int update(long lessonId, int groupId, int teacherId, long startTime, long endTime, int length,
                      String comment, int partitionId) {
        return sql.update(LESSON)
                .set(LESSON.GROUP_ID, groupId)
                .set(LESSON.TEACHER_ID, teacherId)
                .set(LESSON.TIME_OF_START, startTime)
                .set(LESSON.TIME_OF_END, endTime)
                .set(LESSON.DURATION_IN_MINUTES, length)
                .set(LESSON.COMMENT, comment)
                .where(idAndPartition(lessonId, partitionId))
                .execute();
    }


    @Override
    public int delete(long lessonId, int partitionId) {
        return sql.delete(LESSON)
                .where(idAndPartition(lessonId, partitionId))
                .execute();
    }

    @Override
    public boolean isOverlapping(long lessonId, int teacherId, long startTime, long endTime, int partitionId) {
        return isOverlapping(LESSON.ID.ne(lessonId), teacherId, startTime, endTime, partitionId);
    }

    @Override
    public boolean isOverlapping(int teacherId, long startTime, long endTime, int partitionId) {
        return isOverlapping(trueCondition(), teacherId, startTime, endTime, partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public LessonRepositoryImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

    private Condition idAndPartition(long lessonId, int partitionId) {
        return LESSON.ID.eq(lessonId).and(LESSON.PARTITION_ID.eq(partitionId));
    }

    private boolean isOverlapping(Condition prefixCondition, int teacherId, long startTime, long endTime, int partitionId) {
        Condition aLessonForSameTeacher = LESSON.TEACHER_ID.eq(teacherId)
                .and(LESSON.PARTITION_ID.eq(partitionId));

        Condition aLessonStartsBetweenThisLesson = LESSON.TIME_OF_START.between(startTime, endTime);
        Condition aLessonEndsBetweenThisLesson = LESSON.TIME_OF_END.between(startTime, endTime);
        Condition thisLessonStartsBetweenALesson = LESSON.TIME_OF_START.le(startTime)
                .and(LESSON.TIME_OF_END.ge(startTime));
        // No need to check for end time, because it is automatically caught by the first two conditions as well

        Condition lessonIsOverlapping = aLessonForSameTeacher
                .and(aLessonStartsBetweenThisLesson
                        .or(aLessonEndsBetweenThisLesson)
                        .or(thisLessonStartsBetweenALesson));

        return sql.fetchExists(LESSON, prefixCondition.and(lessonIsOverlapping));
    }

}
