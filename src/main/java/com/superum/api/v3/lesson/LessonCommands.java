package com.superum.api.v3.lesson;

import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.api.v2.lesson.InvalidLessonException;
import com.superum.api.v2.lesson.LessonNotFoundException;
import com.superum.api.v2.lesson.OverlappingLessonException;
import com.superum.api.v2.lesson.UnsafeLessonDeleteException;
import com.superum.exception.DatabaseException;
import org.springframework.dao.DataAccessException;

/**
 * <pre>
 * Defines operations which allow to change the state of the system in regards to lessons, i.e. create a new lesson
 *
 * The methods should not return anything (void), unless doing so will require substantial additional effort by
 * consumers to retrieve some information, i.e. database generated ids
 * </pre>
 */
public interface LessonCommands {

    /**
     * <pre>
     * Creates a new lesson in the database
     *
     * Please refer to APIv2.md for explanation on partitionId
     * </pre>
     * @return the created lesson
     * @throws InvalidLessonException if supplied lesson is invalid for creation
     * @throws GroupNotFoundException if supplied lesson has groupId which does not exist in the database
     * @throws OverlappingLessonException if this lesson overlaps with others for its teacher
     * @throws DatabaseException if database error occurred
     * @throws DataAccessException if an unexpected database error occurred
     */
    FetchedLesson create(SuppliedLessonWithTimestamp suppliedLesson, int partitionId);

    /**
     * <pre>
     * Updates a lesson in the database
     *
     * Please refer to APIv2.md for explanation on partitionId
     * </pre>
     * @throws InvalidLessonException if supplied lesson is invalid for updating
     * @throws LessonNotFoundException if supplied lessonId does not exist in the database
     * @throws GroupNotFoundException if supplied lesson has groupId which does not exist in the database
     * @throws OverlappingLessonException if this lesson overlaps with others for its teacher
     * @throws DatabaseException if database error occurred
     * @throws DataAccessException if an unexpected database error occurred
     */
    void update(long lessonId, SuppliedLessonWithTimestamp suppliedLesson, int partitionId);

    /**
     * <pre>
     * Deletes a lesson in the database
     *
     * Please refer to APIv2.md for explanation on partitionId
     * </pre>
     * @throws LessonNotFoundException if supplied lessonId does not exist in the database
     * @throws UnsafeLessonDeleteException if supplied lessonId is still used in other database tables
     * @throws DatabaseException if database error occurred
     * @throws DataAccessException if an unexpected database error occurred
     */
    void delete(long lessonId, int partitionId);

}
