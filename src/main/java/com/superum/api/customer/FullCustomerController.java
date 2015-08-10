package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.helper.PartitionAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

/**
 * <pre>
 * API v2
 * Manages all requests for Customers
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/customer")
public class FullCustomerController {

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
	@ResponseBody
	public FullCustomer createCustomer(PartitionAccount account, @RequestBody FullCustomer customer) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot be null");

        LOG.info("User {} is creating FullCustomer {}", account, customer);

        FullCustomer createdCustomer = fullCustomerService.createCustomer(customer, account.partitionId());
        LOG.info("Successfully created FullCustomer: {}", createdCustomer);

        return createdCustomer;
	}

    @RequestMapping(value = "/{customerId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
	public FullCustomer readCustomer(PartitionAccount account, @PathVariable int customerId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        LOG.info("User {} is reading FullCustomer with id {}", account, customerId);

        FullCustomer customer = fullCustomerService.readCustomer(customerId, account.partitionId());
        LOG.info("Successfully read FullCustomer: {}", customer);

        return customer;
	}

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullCustomer updateCustomer(PartitionAccount account, @RequestBody FullCustomer customer) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot be null");

        LOG.info("User {} is updating FullCustomer {}", account, customer);

        FullCustomer updatedCustomer = fullCustomerService.updateCustomer(customer, account.partitionId());
        LOG.info("Successfully updated FullCustomer: {}", updatedCustomer);

        return updatedCustomer;
    }

    @RequestMapping(value = "/delete/{customerId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullCustomer deleteCustomer(PartitionAccount account, @PathVariable int customerId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        LOG.info("User {} is deleting FullCustomer with id {}", account, customerId);

        FullCustomer deletedCustomer = fullCustomerService.deleteCustomer(customerId, account.partitionId());
        LOG.info("Successfully deleted FullCustomer: {}", deletedCustomer);

        return deletedCustomer;
    }

    @RequestMapping(value = "/for/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FullCustomer> readCustomersForTeacher(PartitionAccount account, @PathVariable int teacherId,
                                                      @RequestParam(value="page", required=false) Integer page,
                                                      @RequestParam(value="per_page", required=false) Integer per_page) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        /**
         * Defaults are set up below rather than in annotation because otherwise
         *
         *      ...?page=&per_page=
         *
         * would cause a NumberFormatException instead of using default values; this is fixed by:
         * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
         *    fail to parse an empty string;
         * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
         *    and would instead propagate down;
         */
        page = ensurePageNotNull(page);
        page--; //Pages start with 1 in the URL, but start with 0 in the app logic

        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        per_page = ensurePerPageNotNull(per_page);

        if (per_page <= 0 || per_page > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + per_page);

        LOG.info("User {} is reading FullCustomers for teacher with id {}", account, teacherId);
        LOG.info("Reading page {}, with {} entries per page", page, per_page);

        List<FullCustomer> customers = fullCustomerService.readCustomersForTeacher(teacherId, page, per_page, account.partitionId());
        LOG.info("Successfully read FullCustomers: {}", customers);

        return customers;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FullCustomer> readCustomersAll(PartitionAccount account,
                                               @RequestParam(value="page", required=false) Integer page,
                                               @RequestParam(value="per_page", required=false) Integer per_page) {
        /**
         * Defaults are set up below rather than in annotation because otherwise
         *
         *      ...?page=&per_page=
         *
         * would cause a NumberFormatException instead of using default values; this is fixed by:
         * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
         *    fail to parse an empty string;
         * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
         *    and would instead propagate down;
         */
        page = ensurePageNotNull(page);
        page--; //Pages start with 1 in the URL, but start with 0 in the app logic

        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        per_page = ensurePerPageNotNull(per_page);

        if (per_page <= 0 || per_page > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + per_page);

        LOG.info("User {} is reading all FullCustomers", account);
        LOG.info("Reading page {}, with {} entries per page", page, per_page);

        List<FullCustomer> customers = fullCustomerService.readCustomersAll(page, per_page, account.partitionId());
        LOG.info("Successfully read FullCustomers: {}", customers);

        return customers;
    }

    @RequestMapping(value = "/for/teacher/{teacherId:[\\d]+}/count", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countCustomersForTeacher(PartitionAccount account, @PathVariable int teacherId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        LOG.info("User {} is counting FullCustomers for teacher with id {}", account, teacherId);

        int count = fullCustomerService.countForTeacher(teacherId, account.partitionId());
        LOG.info("Amount of FullCustomers: {}", count);

        return count;
    }

    @RequestMapping(value = "/all/count", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countCustomers(PartitionAccount account) {
        LOG.info("User {} is counting FullCustomers", account);

        int count = fullCustomerService.countAll(account.partitionId());
        LOG.info("Amount of FullCustomers: {}", count);

        return count;
    }

    @RequestMapping(value = "/exists", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullCustomer customerExists(PartitionAccount account, @RequestBody FullCustomer customer) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot be null");

        LOG.info("User {} is checking if customer {} exists", account, customer);

        FullCustomer existingCustomer = fullCustomerService.exists(customer, account.partitionId());
        LOG.info("Customer retrieved: {}", existingCustomer);

        return existingCustomer;
    }
	
	// CONSTRUCTORS
	
	@Autowired
	public FullCustomerController(FullCustomerService fullCustomerService) {
		this.fullCustomerService = fullCustomerService;
	}
	
	// PRIVATE
	
	private final FullCustomerService fullCustomerService;

    private Integer ensurePageNotNull(Integer page) {
        return (page == null ? DEFAULT_PAGE : page);
    }

    private Integer ensurePerPageNotNull(Integer per_page) {
        return per_page == null ? DEFAULT_PER_PAGE : per_page;
    }

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PER_PAGE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(FullCustomerController.class);

}
