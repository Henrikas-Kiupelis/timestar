package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.customer.Customer;
import com.superum.db.customer.CustomerService;
import com.superum.db.customer.lang.CustomerLanguages;
import com.superum.db.customer.lang.CustomerLanguagesService;
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

        Customer insertedCustomer = customerService.addCustomer(fullCustomer.toCustomer(), partitionId);

        FullCustomer insertedFullCustomer = fullCustomer.withId(insertedCustomer.getId());
        customerLanguagesService.addLanguagesToCustomerContract(insertedFullCustomer.toCustomerLanguages(), partitionId);
        LOG.debug("Customer created: {}", insertedFullCustomer);

        return insertedFullCustomer;
    }

    @Override
    public FullCustomer readCustomer(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        LOG.debug("Reading customer by id: {}", customerId);

        Customer customer = customerService.findCustomer(customerId, partitionId);
        CustomerLanguages customerLanguages = customerLanguagesService.getLanguagesForCustomerContract(customerId, partitionId);
        FullCustomer fullCustomer = new FullCustomer(customer, customerLanguages);
        LOG.debug("Customer retrieved: {}", fullCustomer);

        return fullCustomer;
    }

    @Override
    public FullCustomer updateCustomer(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");



        return null;
    }

    @Override
    public FullCustomer deleteCustomer(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        return null;
    }

    @Override
    public List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        return null;
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        return null;
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        return 0;
    }

    @Override
    public int countAll(int partitionId) {
        return 0;
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerServiceImpl(FullCustomerQueries fullCustomerQueries, CustomerService customerService, CustomerLanguagesService customerLanguagesService) {
        this.fullCustomerQueries = fullCustomerQueries;
        this.customerService = customerService;
        this.customerLanguagesService = customerLanguagesService;
    }

    // PRIVATE

    private final FullCustomerQueries fullCustomerQueries;
    private final CustomerService customerService;
    private final CustomerLanguagesService customerLanguagesService;

    private static final Logger LOG = LoggerFactory.getLogger(FullCustomerService.class);

}
