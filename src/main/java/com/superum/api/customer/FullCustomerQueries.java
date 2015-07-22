package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
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
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if customer has no fields set
     * @throws DatabaseException if database error occurred
     */
    boolean exists(FullCustomer customer, int partitionId);

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

}
