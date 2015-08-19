package com.superum.api.customer;

import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.exception.DatabaseException;
import com.superum.helper.jooq.DefaultCommands;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.ForeignQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ValidCustomerCommandServiceImpl implements ValidCustomerCommandService {

    @Override
    public ValidCustomerDTO create(ValidCustomerDTO validCustomerDTO, int partitionId) {
        ValidCustomer validCustomer = new ValidCustomer(validCustomerDTO);

        if (validCustomer.hasId())
            throw new InvalidCustomerException("Provided customer has its id set; please unset it or use POST instead!");

        if (!validCustomer.canBeInserted())
            throw new InvalidCustomerException("Provided customer does not have the following mandatory fields set: "
                    + validCustomer.missingMandatoryFieldNames().join(", "));

        return defaultCustomerCommands.create(validCustomer, partitionId, ValidCustomerDTO::valueOf)
                .orElseThrow(() -> new DatabaseException("Couldn't return customer after inserting it: " + validCustomer));
    }

    @Override
    public void update(ValidCustomerDTO validCustomerDTO, int partitionId) {
        ValidCustomer validCustomer = new ValidCustomer(validCustomerDTO);

        if (!validCustomer.hasId())
            throw new InvalidCustomerException("Provided customer doesn't have its id set; please set it or use PUT instead!");

        if (!validCustomer.updateFields().findAny().isPresent())
            throw new InvalidCustomerException("Provided customer only has its id set; to update this customer, set additional fields!");

        if (!defaultCustomerQueries.exists(validCustomer.getId(), partitionId))
            throw new CustomerNotFoundException("Couldn't find customer with id " + validCustomer.getId());

        if (defaultCustomerCommands.update(validCustomer, partitionId) == 0)
            throw new DatabaseException("Couldn't update customer: " + validCustomer);
    }

    @Override
    public void delete(int customerId, int partitionId) {
        if (!defaultCustomerQueries.exists(customerId, partitionId))
            throw new CustomerNotFoundException("Couldn't find customer with id " + customerId);

        if (foreignCustomerQueries.isUsed(customerId))
            throw new UnsafeCustomerDeleteException("Cannot delete customer with id " + customerId +
                    " while it still has entries in other tables");

        if (defaultCustomerCommands.delete(customerId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete customer with id: " + customerId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidCustomerCommandServiceImpl(DefaultCommands<CustomerRecord, Integer> defaultCustomerCommands,
                                           DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries,
                                           ForeignQueries<Integer> foreignCustomerQueries) {
        this.defaultCustomerCommands = defaultCustomerCommands;
        this.defaultCustomerQueries = defaultCustomerQueries;
        this.foreignCustomerQueries = foreignCustomerQueries;
    }

    // PRIVATE

    private final DefaultCommands<CustomerRecord, Integer> defaultCustomerCommands;
    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;
    private final ForeignQueries<Integer> foreignCustomerQueries;

}
