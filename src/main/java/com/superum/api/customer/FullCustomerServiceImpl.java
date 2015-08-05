package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.customer.Customer;
import com.superum.db.customer.CustomerService;
import com.superum.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullCustomerServiceImpl implements FullCustomerService {

    @Override
    public FullCustomer createCustomer(FullCustomer fullCustomer, int partitionId) {
        if (fullCustomer == null)
            throw new InvalidRequestException("Customer cannot be null");

        if (fullCustomer.hasId())
            throw new InvalidCustomerException("Provided customer has its id set; please unset it or use /update instead!");

        if (!fullCustomer.canBeInserted())
            throw new InvalidCustomerException("Provided customer does not have the following mandatory fields set: " + fullCustomer.missingMandatoryFieldNames());

        LOG.debug("Creating new customer: {}", fullCustomer);

        Customer insertedCustomer = customerService.addCustomer(fullCustomer.toCustomer(), partitionId);
        FullCustomer insertedFullCustomer = fullCustomer.withId(insertedCustomer.getId());
        LOG.debug("Customer created: {}", insertedFullCustomer);

        return insertedFullCustomer;
    }

    @Override
    public FullCustomer readCustomer(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        LOG.debug("Reading customer with id: {}", customerId);

        Customer customer = customerService.findCustomer(customerId, partitionId);
        FullCustomer fullCustomer = new FullCustomer(customer);
        LOG.debug("Customer retrieved: {}", fullCustomer);

        return fullCustomer;
    }

    @Override
    public FullCustomer updateCustomer(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot be null");

        if (!customer.hasId())
            throw new InvalidCustomerException("Provided customer doesn't have its id set; please set it or use /create instead!");

        if (!customer.canUpdateCustomer())
            throw new InvalidCustomerException("Provided customer only has its id set; to update this customer, set additional fields!");

        LOG.debug("Updating customer: {}",  customer);

        FullCustomer oldCustomer = readCustomer(customer.getId(), partitionId);
        if (!customer.hasEqualSetCustomerFields(oldCustomer))
            try {
                if (fullCustomerQueries.updatePartial(customer, partitionId) == 0)
                    throw new DatabaseException("Couldn't update customer: " + customer);
            } catch (DataAccessException e) {
                throw new DatabaseException("An unexpected error occurred when trying to update customer: " + customer, e);
            }
        LOG.debug("Customer has been updated; before update: {}", oldCustomer);

        return oldCustomer;
    }

    @Override
    public FullCustomer deleteCustomer(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        LOG.debug("Deleting customer with id: {}", customerId);

        FullCustomer deletedCustomer = readCustomer(customerId, partitionId);
        try {
            if (fullCustomerQueries.safeDelete(customerId, partitionId) == 0)
                throw new DatabaseException("Couldn't delete customer with id: " + customerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete customer with id " + customerId, e);
        }
        LOG.debug("Customer has been deleted: {}", deletedCustomer);

        return deletedCustomer;
    }

    @Override
    public List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        LOG.debug("Reading customers for teacher with id {}; page {}, amount {}", teacherId, page, amount);

        List<FullCustomer> customers;
        try {
            customers = fullCustomerQueries.readCustomersForTeacher(teacherId, page, amount, partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers for teacher with id " + teacherId, e);
        }
        LOG.debug("Customers retrieved: {}", customers);

        return customers;
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        LOG.debug("Reading all customers; page {}, amount {}", page, amount);

        List<FullCustomer> customers;
        try {
            customers = fullCustomerQueries.readCustomersAll(page, amount, partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers", e);
        }
        LOG.debug("Customers retrieved: {}", customers);

        return customers;
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        int count;
        try {
            count = fullCustomerQueries.countForTeacher(teacherId, partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to count all customers for teacher with id " + teacherId, e);
        }
        LOG.debug("Teacher with id {} has {} customers", teacherId, count);

        return count;
    }

    @Override
    public int countAll(int partitionId) {
        int count;
        try {
            count = fullCustomerQueries.count(partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to count all customers", e);
        }
        LOG.debug("There are a total of {} customers", count);

        return count;
    }

    @Override
    public FullCustomer exists(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot be null");

        if (!customer.hasAnyFieldsSet())
            throw new InvalidCustomerException("Provided customer does not have any fields set, so checking if it exists is impossible");

        LOG.debug("Checking if a customer exists: {}", customer);

        int customerId;
        try {
            customerId = fullCustomerQueries.exists(customer, partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to check if this customer exists: " + customer, e);
        }
        FullCustomer existingCustomer = readCustomer(customerId, partitionId);
        LOG.debug("Found customer: {}", existingCustomer);

        return existingCustomer;
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
