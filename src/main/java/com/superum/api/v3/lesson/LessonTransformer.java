package com.superum.api.v3.lesson;

import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.api.v2.lesson.InvalidLessonException;
import com.superum.api.v2.lesson.LessonNotFoundException;
import org.jooq.Record;

/**
 * Transforms various lesson representations to others
 */
public interface LessonTransformer {

    /**
     * <pre>
     * JSON -> domain object
     *
     * Ensures the supplied lesson contains valid data for creation purposes
     * </pre>
     * @throws InvalidLessonException if supplied lesson is invalid for creation
     * @throws GroupNotFoundException if supplied lesson has groupId which does not exist in the database
     */
    Lesson from(SuppliedLessonWithTimestamp lessonToCreate);

    /**
     * <pre>
     * JSON -> domain object
     *
     * Ensures the supplied lesson contains valid data for updating purposes
     * </pre>
     * @throws InvalidLessonException if supplied lesson is invalid for updating
     * @throws GroupNotFoundException if supplied lesson has groupId which does not exist in the database
     * @throws LessonNotFoundException if supplied lessonId does not exist in the database
     */
    Lesson from(SuppliedLessonWithTimestamp lessonToUpdate, long lessonId);

    /**
     * <pre>
     * JSON -> JSON
     *
     * Computes timestamp value from given timezone, date, hour and minute values
     * </pre>
     * @throws InvalidLessonException if the timezone, date, hour or minute values are invalid
     */
    SuppliedLessonWithTimestamp from(SuppliedLessonWithTimezone lesson, boolean toCreate);

    /**
     * <pre>
     * JSON -> JSON
     *
     * Computes timestamp value only if it was not given
     * </pre>
     * @throws InvalidLessonException if the timestamp was not given, and timezone, date, hour or minute values are invalid
     */
    SuppliedLessonWithTimestamp from(SuppliedLesson lesson, boolean toCreate);

    /**
     * Database -> JSON
     */
    FetchedLesson from(Record record);

}
