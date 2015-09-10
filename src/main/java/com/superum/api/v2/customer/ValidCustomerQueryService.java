package com.superum.api.v2.customer;

import com.superum.api.v2.teacher.TeacherNotFoundException;
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
public interface ValidCustomerQueryService {

    /**
     * <pre>
     * Reads a customer with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return customer that was read
     *
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    ValidCustomerDTO readById(int customerId, int partitionId);

    /**
     * <pre>
     * Reads all Customers for a teacher with specified id; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * Customers are chosen in the following manner:
     * 1) Customers have students;
     * 2) Students belong to groups;
     * 3) Teachers are responsible for groups;
     * 4) Therefore, if a Customer has a student, which belongs to a group the specified teacher is responsible for,
     *    this Customer is added to the list
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return customers that were read
     *
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidCustomerDTO> readForTeacher(int teacherId, int page, int amount, int partitionId);

    /**
     * <pre>
     * Reads all Customers; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return customers that were read
     *
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<ValidCustomerDTO> readAll(int page, int amount, int partitionId);

    /**
     * <pre>
     * Counts the total amount of customers for a teacher with specified id right now;
     *
     * Customers are chosen in the following manner:
     * 1) Customers have students;
     * 2) Students belong to groups;
     * 3) Teachers are responsible for groups;
     * 4) Therefore, if a Customer has a student, which belongs to a group the specified teacher is responsible for,
     *    this Customer is added to the list
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return count of customers for a teacher with specified id
     *
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    int countForTeacher(int teacherId, int partitionId);

    /**
     * <pre>
     * Counts the total amount of customers right now;
     *
     * This number MUST NOT be used to reason about customerId - it is entirely possible some ids are missing, i.e.
     * due to deletion.
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return total count of customers
     *
     * @throws DataAccessException if an unexpected database error occurred
     */
    int countAll(int partitionId);

}
