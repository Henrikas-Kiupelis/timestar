package com.superum.api.group;

import com.superum.api.customer.CustomerNotFoundException;
import com.superum.api.student.StudentNotFoundException;
import com.superum.api.teacher.TeacherNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * Responsible for handling group queries
 *
 * Uses CQRS model, where queries only read data, but do not have any other side effects
 * </pre>
 */
@Service
public interface ValidGroupQueryService {

    /**
     * <pre>
     * Reads and returns a group with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return group that was read;
     *
     * @throws GroupNotFoundException if no group with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    ValidGroupDTO readById(int groupId, int partitionId);

    /**
     * <pre>
     * Reads all groups; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return groups that were read; empty list if no groups exist
     *
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidGroupDTO> readAll(int page, int amount, int partitionId);

    /**
     * <pre>
     * Reads groups for a teacher with specified id; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return groups that were read; empty list if no such groups exist
     *
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidGroupDTO> readForTeacher(int teacherId, int page, int amount, int partitionId);

    /**
     * <pre>
     * Reads groups for a customer with specified id; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return groups that were read; empty list if no such groups exist
     *
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidGroupDTO> readForCustomer(int customerId, int page, int amount, int partitionId);

    /**
     * <pre>
     * Reads groups for a student with specified id; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return groups that were read; empty list if no such groups exist
     *
     * @throws StudentNotFoundException if no student with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidGroupDTO> readForStudent(int studentId, int page, int amount, int partitionId);

}
