package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.exception.DatabaseException;
import org.springframework.stereotype.Service;

@Service
public interface ValidCustomerCommandService {

    /**
     * <pre>
     * Creates a new customer
     *
     * The id fields must not be set, but all the other mandatory fields must be set
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return the created customer with id field now set
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if id field was set or a mandatory field was not set
     * @throws DatabaseException if database error occurred
     */
    ValidCustomerDTO create(ValidCustomerDTO validCustomerDTO, int partitionId);

    /**
     * <pre>
     * Updates an existing customer
     *
     * The id fields must be set, and at least one more field must be set
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if customer is null
     * @throws InvalidCustomerException if id field was not set, or no other fields were set
     * @throws CustomerNotFoundException if a customer with specified id does not exist
     * @throws DatabaseException if database error occurred
     */
    void update(ValidCustomerDTO validCustomerDTO, int partitionId);

    /**
     * <pre>
     * Deletes a customer with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DatabaseException if database error occurred
     */
    void delete(int customerId, int partitionId);

}
