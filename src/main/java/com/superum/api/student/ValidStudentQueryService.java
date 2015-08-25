package com.superum.api.student;

import com.superum.api.customer.CustomerNotFoundException;
import com.superum.api.group.GroupNotFoundException;
import com.superum.api.lesson.LessonNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * Responsible for handling student queries
 *
 * Uses CQRS model, where queries only read data, but do not have any other side effects
 * </pre>
 */
@Service
public interface ValidStudentQueryService {

    /**
     * <pre>
     * Reads a student with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return student that was read
     *
     * @throws StudentNotFoundException if no student with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    ValidStudentDTO readById(int studentId, int partitionId);

    /**
     * <pre>
     * Reads all students; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return students that were read
     *
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidStudentDTO> readAll(int page, int amount, int partitionId);

    /**
     * <pre>
     * Reads all students for a group; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return students that were read
     *
     * @throws GroupNotFoundException if no group with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidStudentDTO> readForGroup(int groupId, int page, int amount, int partitionId);

    /**
     * <pre>
     * Reads all students that attended a certain lesson; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return students that were read
     *
     * @throws LessonNotFoundException if no lesson with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidStudentDTO> readForLesson(long lessonId, int page, int amount, int partitionId);

    /**
     * <pre>
     * Reads all students for a customer; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return students that were read
     *
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidStudentDTO> readForCustomer(int customerId, int page, int amount, int partitionId);

}
