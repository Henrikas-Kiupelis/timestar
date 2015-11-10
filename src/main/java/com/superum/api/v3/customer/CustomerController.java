package com.superum.api.v3.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import com.superum.api.v3.customer.dto.SuppliedCustomer;
import eu.goodlike.misc.CommonControllerLogic;
import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/timestar/api/v3/customer")
public class CustomerController implements CommonControllerLogic {

// COMMANDS

    @RequestMapping(method = POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FetchedCustomer create(@RequestBody SuppliedCustomer suppliedCustomer) {
        Null.check(suppliedCustomer).ifAny(() -> new InvalidRequestException("Customer cannot be null"));
        LOG.info("Create customer request: {}", suppliedCustomer);

        FetchedCustomer createdCustomer = customerCommands.create(suppliedCustomer);
        LOG.info("Successfully created customer: {}", createdCustomer);

        return createdCustomer;
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = PUT, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(@PathVariable int id, @RequestBody SuppliedCustomer suppliedCustomer) {
        Null.check(suppliedCustomer).ifAny(() -> new InvalidRequestException("Customer cannot be null"));
        LOG.info("Update customer request: {}", suppliedCustomer);

        customerCommands.update(suppliedCustomer, id);
        LOG.info("Customer successfully updated");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    @ResponseBody
    public void delete(@PathVariable int id) {
        validateId("Customer", id, InvalidRequestException::new);
        LOG.info("Delete customer with id request: {}", id);

        customerCommands.delete(id);
        LOG.info("Customer successfully deleted");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FetchedCustomer read(@PathVariable int id) {
        validateId("Customer", id, InvalidRequestException::new);
        LOG.info("Read customer with id request: {}", id);

        FetchedCustomer customer = customerQueries.readById(id);
        LOG.info("Customer retrieved: {}", customer);

        return customer;
    }

    @RequestMapping(value = "/teacher/{teacherId:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FetchedCustomer> readForTeacher(@PathVariable int teacherId,
                                                 @RequestParam(value="page", required=false) Integer page,
                                                 @RequestParam(value="per_page", required=false) Integer per_page) {
        validateId("Teacher", teacherId, InvalidRequestException::new);
        page = validatePage(page, InvalidRequestException::new);
        per_page = validatePerPage(per_page, InvalidRequestException::new);
        LOG.info("Reading customers for teacher with id {}; page: {}, per_page: {}", teacherId, page, per_page);

        List<FetchedCustomer> customers = customerQueries.readForTeacher(teacherId, page, per_page);
        LOG.info("Customers retrieved: {}", customers);

        return customers;
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FetchedCustomer> readAll(@RequestParam(value="page", required=false) Integer page,
                                          @RequestParam(value="per_page", required=false) Integer per_page) {
        page = validatePage(page, InvalidRequestException::new);
        per_page = validatePerPage(per_page, InvalidRequestException::new);
        LOG.info("Reading all customers; page: {}, per_page: {}", page, per_page);

        List<FetchedCustomer> customers = customerQueries.readAll(page, per_page);
        LOG.info("Customers retrieved: {}", customers);

        return customers;
    }

    @RequestMapping(value = "/teacher/{teacherId:[\\d]+}/count", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countForTeacher(@PathVariable int teacherId) {
        validateId("Teacher", teacherId, InvalidRequestException::new);
        LOG.info("Counting customers for teacher with id {}", teacherId);

        int count = customerQueries.countForTeacher(teacherId);
        LOG.info("Amount of customers: {}", count);

        return count;
    }

    @RequestMapping(value = "/count", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countAll() {
        LOG.info("Counting all customers");

        int count = customerQueries.countAll();
        LOG.info("Amount of customers: {}", count);

        return count;
    }

    // CONSTRUCTORS

    @Autowired
    public CustomerController(CustomerCommands customerCommands, CustomerQueries customerQueries) {
        this.customerCommands = customerCommands;
        this.customerQueries = customerQueries;
    }

    // PRIVATE

    private final CustomerCommands customerCommands;
    private final CustomerQueries customerQueries;

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

}
