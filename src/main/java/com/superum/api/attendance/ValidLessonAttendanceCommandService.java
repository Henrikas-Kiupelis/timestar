package com.superum.api.attendance;

import com.superum.api.lesson.LessonNotFoundException;
import com.superum.api.student.StudentNotFoundException;
import com.superum.exception.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public interface ValidLessonAttendanceCommandService {

    /**
     * <pre>
     * Creates a lesson attendance record
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @throws LessonNotFoundException if lesson with given id doesn't exist
     * @throws StudentNotFoundException if student for any given id doesn't exist
     * @throws InvalidLessonAttendanceException if any of given students don't belong to the group this lesson was for
     * @throws DuplicateLessonAttendanceException if this lesson already has an attendance record
     * @throws DatabaseException if database error occurred
     * @throws DataAccessException if an unexpected database error occurred
     */
    void create(ValidLessonAttendanceDTO validLessonAttendanceDTO, int partitionId);

    /**
     * <pre>
     * Updates a lesson attendance record
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @throws LessonNotFoundException if lesson with given id doesn't exist
     * @throws StudentNotFoundException if student for any given id doesn't exist
     * @throws InvalidLessonAttendanceException if any of given students don't belong to the group this lesson was for
     * @throws LessonAttendanceNotFoundException if this lesson doesn't already have an attendance record
     * @throws DatabaseException if database error occurred
     * @throws DataAccessException if an unexpected database error occurred
     */
    void update(ValidLessonAttendanceDTO validLessonAttendanceDTO, int partitionId);

    /**
     * <pre>
     * Deletes all lesson attendance records for a lesson with given id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @throws LessonNotFoundException if lesson with given id doesn't exist
     * @throws LessonAttendanceNotFoundException if this lesson doesn't have an attendance record
     * @throws DatabaseException if database error occurred
     * @throws DataAccessException if an unexpected database error occurred
     */
    void deleteForLesson(long lessonId, int partitionId);

    /**
     * <pre>
     * Deletes all lesson attendance records for a student with given id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @throws StudentNotFoundException if student with given id doesn't exist
     * @throws LessonAttendanceNotFoundException if this student doesn't have an attendance record
     * @throws DatabaseException if database error occurred
     * @throws DataAccessException if an unexpected database error occurred
     */
    void deleteForStudent(int studentId, int partitionId);

}
