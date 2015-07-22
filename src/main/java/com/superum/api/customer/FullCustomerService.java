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
     * Creates a new customer if the id field is set
     * Updates an existing customer if the id field is not set
     *
     * If any other fields are not set when creating, an exception might be thrown
     * If any other fields are not set when updating, they are ignored
     *
     * Returns the inserted customer with all missing fields now set
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if attempt to create was made, yet a mandatory field was not set
     * @throws CustomerNotFoundException if attempt to update was made, yet the customer with specified id does not exist
     * @throws DatabaseException if database error occurred
     */
    FullCustomer insert(FullCustomer customer, int partitionId);

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
    FullCustomer read(int customerId, int partitionId);

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
    FullCustomer delete(int customerId, int partitionId);

    /**
     * <pre>
     * Reads all Customers for a teacher with specified id
     *
     * Customers that are chosen in the following manner:
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
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DatabaseException if database error occurred
     */
    List<FullCustomer> readAllForTeacher(int teacherId, int partitionId);

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
     * @throws InvalidRequestException if amount is illegal (<=0 || >100)
     * @throws DatabaseException if database error occurred
     */
    List<FullCustomer> all(int page, int amount, int partitionId);

}
