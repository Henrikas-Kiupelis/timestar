package com.superum.api.lesson;

import com.superum.api.customer.CustomerNotFoundException;
import com.superum.api.group.GroupNotFoundException;
import com.superum.api.student.StudentNotFoundException;
import com.superum.api.teacher.TeacherNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * Responsible for handling customer queries
 *
 * Uses CQRS model, where queries only read data, but do not have any other side effects
 * </pre>
 */
@Service
public interface ValidLessonQueryService {

    /**
     * <pre>
     * Reads and returns a lesson with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return lesson that was read;
     *
     * @throws LessonNotFoundException if no lesson with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    ValidLessonDTO readById(long lessonId, int partitionId);

    /**
     * <pre>
     * Reads all lessons; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return lessons that were read; empty list if no lessons exist
     *
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidLessonDTO> readAll(int page, int amount, long start, long end, int partitionId);

    /**
     * <pre>
     * Reads all lessons for a group with specified id; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return lessons that were read; empty list if no such lessons exist
     *
     * @throws GroupNotFoundException if no group with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidLessonDTO> readForGroup(int groupId, int page, int amount, long start, long end, int partitionId);

    /**
     * <pre>
     * Reads all lessons for a teacher with specified id; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return lessons that were read; empty list if no such lessons exist
     *
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidLessonDTO> readForTeacher(int teacherId, int page, int amount, long start, long end, int partitionId);

    /**
     * <pre>
     * Reads all lessons for a customer with specified id; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return lessons that were read; empty list if no such lessons exist
     *
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidLessonDTO> readForCustomer(int customerId, int page, int amount, long start, long end, int partitionId);

    /**
     * <pre>
     * Reads all lessons that a student has attended; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return lessons that were read; empty list if no such lessons exist
     *
     * @throws StudentNotFoundException if no student with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidLessonDTO> readForStudent(int studentId, int page, int amount, long start, long end, int partitionId);

}
