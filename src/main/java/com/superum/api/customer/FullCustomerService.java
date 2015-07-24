package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.exception.DatabaseException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * Responsible for handling the app logic in regard to customers
 * </pre>
 */
@Service
public interface FullCustomerService {

    /**
     * <pre>
     * Creates a new customer
     *
     * The id fields must not be set, but all the other mandatory fields must be set
     *
     * Returns the created customer with id field now set
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if id field was set or a mandatory field was not set
     * @throws DatabaseException if database error occurred
     */
    FullCustomer createCustomer(FullCustomer customer, int partitionId);

    /**
     * <pre>
     * Reads a customer with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DatabaseException if database error occurred
     */
    FullCustomer readCustomer(int customerId, int partitionId);

    /**
     * <pre>
     * Updates an existing customer
     *
     * The id fields must be set, and at least one more field must be set
     *
     * Returns the customer data before update, as if read(customerId) was called before updating
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if id field was not set, or no other fields were set
     * @throws CustomerNotFoundException if a customer with specified id does not exist
     * @throws DatabaseException if database error occurred
     */
    FullCustomer updateCustomer(FullCustomer customer, int partitionId);

    /**
     * <pre>
     * Deletes a customer with specified id
     *
     * The deleted customer data is returned, as if read(customerId) was called before deleting
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DatabaseException if database error occurred
     */
    FullCustomer deleteCustomer(int customerId, int partitionId);

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
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws InvalidRequestException if page is illegal (<0)
     * @throws InvalidRequestException if amount is illegal (<=0 || >100)
     * @throws DatabaseException if database error occurred
     */
    List<FullCustomer> readCustomersAll(int page, int amount, int partitionId);

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
     * Returns the total amount of customers right now;
     *
     * This number MUST NOT be used to reason about customerId - it is entirely possible some ids are missing, i.e.
     * due to deletion.
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws DatabaseException if database error occurred
     */
    int countAll(int partitionId);


}
