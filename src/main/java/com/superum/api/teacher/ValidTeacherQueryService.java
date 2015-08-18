package com.superum.api.teacher;

import com.superum.exception.DatabaseException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * Responsible for handling teacher queries
 *
 * Uses CQRS model, where queries only read data, but do not have any other side effects
 * </pre>
 */
@Service
public interface ValidTeacherQueryService {

    /**
     * <pre>
     * Reads a teacher with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return teacher that was read
     *
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DatabaseException if database error occurred
     */
    FullTeacherDTO read(int teacherId, int partitionId);

    /**
     * <pre>
     * Reads all teachers; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return teachers that were read
     *
     * @throws DatabaseException if database error occurred
     */
    List<FullTeacherDTO> readAll(int page, int amount, int partitionId);

    /**
     * <pre>
     * Counts the total amount of teachers right now;
     *
     * This number MUST NOT be used to reason about teacherId - it is entirely possible some ids are missing, i.e.
     * due to deletion.
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return total count of teachers
     *
     * @throws DatabaseException if database error occurred
     */
    int countAll(int partitionId);

}
