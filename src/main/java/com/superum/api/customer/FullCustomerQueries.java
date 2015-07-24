package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.exception.DatabaseException;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * Responsible for various queries to these database tables:
 * customer
 * customer_lang
 *
 * These queries complement or replace the queries from API v1
 * </pre>
 */
@Repository
public interface FullCustomerQueries {

    /**
     * <pre>
     * Checks if a Customer exists
     *
     * The check is made by using all the set fields (Languages field is ignored)
     *
     * Returns the id field of a customer if one is found, 0 otherwise
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if customer has no fields set
     * @throws DatabaseException if database error occurred
     */
    int exists(FullCustomer customer, int partitionId);

    /**
     * <pre>
     * Updates a Customer
     *
     * Only set fields are updated
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if customer is illegal:
     *          only id field set,
     *          id field not set,
     *          no fields set
     * @throws CustomerNotFoundException if id is set, but no customer with given id exists
     * @throws DatabaseException if database error occurred
     */
    FullCustomer updatePartial(FullCustomer customer, int partitionId);

    /**
     * <pre>
     * Deletes a Customer with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws UnsafeCustomerDeleteException if attempt to delete a Customer was made, but this customer still has valid data
     * @throws DatabaseException if database error occurred
     */
    FullCustomer safeDelete(int customerId, int partitionId);

    /**
     * <pre>
     * Returns the total amount of customers for a teacher with specified id right now;
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
     *
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DatabaseException if database error occurred
     */
    int countForTeacher(int teacherId, int partitionId);

    /**
     * <pre>
     * Returns the total amount of Customers right now;
     *
     * This number MUST NOT be used to reason about customerId - it is entirely possible some ids are missing, i.e.
     * due to deletion.
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws DatabaseException if database error occurred
     */
    int count(int partitionId);

}
