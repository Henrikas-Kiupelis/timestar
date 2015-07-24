package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.customer.Customer;
import com.superum.db.customer.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullCustomerServiceImpl implements FullCustomerService {

    @Override
    public FullCustomer createCustomer(FullCustomer fullCustomer, int partitionId) {
        if (fullCustomer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        if (fullCustomer.hasId())
            throw new InvalidCustomerException("Provided customer has its id set; please unset it or use /update instead!");

        if (!fullCustomer.canBeInserted())
            throw new InvalidCustomerException("Provided customer does not have the following mandatory fields set: " + fullCustomer.missingMandatoryFieldNames());

        LOG.debug("Creating new customer: {}", fullCustomer);

        Customer customer = fullCustomer.toCustomer();
        //customerService.




        return fullCustomer.withId(1);
    }

    @Override
    public FullCustomer readCustomer(int customerId, int partitionId) {
        return null;
    }

    @Override
    public FullCustomer updateCustomer(FullCustomer customer, int partitionId) {
        return null;
    }

    @Override
    public FullCustomer deleteCustomer(int customerId, int partitionId) {
        return null;
    }

    @Override
    public List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId) {
        return null;
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        return null;
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        return 0;
    }

    @Override
    public int countAll(int partitionId) {
        return 0;
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerServiceImpl(FullCustomerQueries fullCustomerQueries, CustomerService customerService) {
        this.fullCustomerQueries = fullCustomerQueries;
        this.customerService = customerService;
    }

    // PRIVATE

    private final FullCustomerQueries fullCustomerQueries;
    private final CustomerService customerService;

    private static final Logger LOG = LoggerFactory.getLogger(FullCustomerService.class);

}
