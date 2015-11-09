package com.superum.api.v3.lesson;

import com.superum.api.v3.lesson.dto.FetchedLesson;
import org.jooq.Record;
import org.springframework.dao.DataAccessException;

import java.util.Optional;
import java.util.function.Function;

/**
 * Wraps SQL access to the database
 */
public interface LessonRepository {

    /**
     * <pre>
     * Inserts a record into LESSON table using given field values
     *
     * Comment is allowed to be null
     * </pre>
     * @return inserted lesson, or empty if retrieval of inserted lesson failed
     * @throws NullPointerException if mapper is null
     * @throws DataAccessException if an unexpected database error occurred
     */
    Optional<FetchedLesson> insert(int groupId, int teacherId, long startTime, long endTime, int length,
                                   String comment, Function<Record, FetchedLesson> mapper);

    /**
     * <pre>
     * Updates a record in LESSON table with id of lessonId using given field values
     *
     * Comment is allowed to be null
     * </pre>
     * @return amount of records updated
     * @throws DataAccessException if an unexpected database error occurred
     */
    int update(long lessonId, int groupId, int teacherId, long startTime, long endTime, int length, String comment);

    /**
     * Deletes a record in LESSOn table with id of lessonId
     * @return amount of records deleted
     * @throws DataAccessException if an unexpected database error occurred
     */
    int delete(long lessonId);

    /**
     * @return true if there are lessons for teacher with id of teacherId between startTime and endTime, with the
     * exception of lesson with id of lessonId; false otherwise
     * @throws DataAccessException if an unexpected database error occurred
     */
    boolean isOverlapping(long lessonId, int teacherId, long startTime, long endTime);

    /**
     * @return true if there are lessons for teacher with id of teacherId between startTime and endTime; false otherwise
     * @throws DataAccessException if an unexpected database error occurred
     */
    boolean isOverlapping(int teacherId, long startTime, long endTime);

}
