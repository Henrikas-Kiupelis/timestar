package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.exception.DatabaseException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return the id field of a customer if such is found
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if customer has no fields set (ignoring languages)
     * @throws DatabaseException if database error occurred
     */
    Optional<Integer> exists(FullCustomer customer, int partitionId);

    /**
     * <pre>
     * Updates a Customer
     *
     * Only set fields are updated
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if id field was not set, or no other fields were set
     * @throws CustomerNotFoundException if id is set, but no customer with given id exists
     * @throws DatabaseException if database error occurred
     */
    void updatePartial(FullCustomer customer, int partitionId);

    /**
     * <pre>
     * Deletes a Customer with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws UnsafeCustomerDeleteException if attempt to delete a Customer was made, but this customer still has valid data
     * @throws DatabaseException if database error occurred
     */
    void safeDelete(int customerId, int partitionId);

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
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws InvalidRequestException if page is illegal (<0)
     * @throws InvalidRequestException if amount is illegal (<=0 || >100)
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DatabaseException if database error occurred
     */
    List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId);

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
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws InvalidRequestException if page is illegal (<0)
     * @throws InvalidRequestException if amount is illegal (<=0 || >100)
     * @throws DatabaseException if database error occurred
     */
    List<FullCustomer> readCustomersAll(int page, int amount, int partitionId);

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
     * @throws DatabaseException if database error occurred
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
     * @throws DatabaseException if database error occurred
     */
    int count(int partitionId);

}
