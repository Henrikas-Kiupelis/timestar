package com.superum.api.v2.customer;

import com.superum.api.core.CommonControllerLogic;
import com.superum.api.exception.InvalidRequestException;
import com.superum.helper.PartitionAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * <pre>
 * API v2
 * Manages all requests for customers
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/customer")
public class ValidCustomerController extends CommonControllerLogic {

    // COMMANDS

	@RequestMapping(method = POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
	@ResponseBody
	public ValidCustomerDTO create(PartitionAccount account, @RequestBody ValidCustomerDTO customer) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot be null");

        LOG.info("User {} is creating customer {}", account, customer);

        ValidCustomerDTO createdCustomer = validCustomerCommandService.create(customer, account.partitionId());
        LOG.info("Successfully created customer: {}", createdCustomer);

        return createdCustomer;
	}

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody ValidCustomerDTO customer) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot be null");

        LOG.info("User {} is updating customer {}", account, customer);

        validCustomerCommandService.update(customer, account.partitionId());
        LOG.info("Customer successfully updated");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    @ResponseBody
    public void delete(PartitionAccount account, @PathVariable int id) {
        validateId("Customer", id);

        LOG.info("User {} is deleting customer with id {}", account, id);

        validCustomerCommandService.delete(id, account.partitionId());
        LOG.info("Customer successfully deleted");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
	public ValidCustomerDTO read(PartitionAccount account, @PathVariable int id) {
        validateId("Customer", id);

        LOG.info("User {} is reading customer with id {}", account, id);

        ValidCustomerDTO customer = validCustomerQueryService.readById(id, account.partitionId());
        LOG.info("Customer retrieved: {}", customer);

        return customer;
	}

    @RequestMapping(value = "/teacher/{teacherId:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidCustomerDTO> readForTeacher(PartitionAccount account, @PathVariable int teacherId,
                                                 @RequestParam(value="page", required=false) Integer page,
                                                 @RequestParam(value="per_page", required=false) Integer per_page) {
        validateId("Teacher", teacherId);
        page = validatePage(page);
        per_page = validatePerPage(per_page);

        LOG.info("User {} is reading customers for teacher with id {}, page {}, with {} entries per page",
                account, teacherId, page, per_page);

        List<ValidCustomerDTO> customers = validCustomerQueryService.readForTeacher(teacherId, page, per_page, account.partitionId());
        LOG.info("Customers retrieved: {}", customers);

        return customers;
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidCustomerDTO> readAll(PartitionAccount account,
                                          @RequestParam(value="page", required=false) Integer page,
                                          @RequestParam(value="per_page", required=false) Integer per_page) {
        page = validatePage(page);
        per_page = validatePerPage(per_page);

        LOG.info("User {} is reading all customers, page {}, with {} entries per page",
                account, page, per_page);

        List<ValidCustomerDTO> customers = validCustomerQueryService.readAll(page, per_page, account.partitionId());
        LOG.info("Customers retrieved: {}", customers);

        return customers;
    }

    @RequestMapping(value = "/teacher/{teacherId:[\\d]+}/count", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countForTeacher(PartitionAccount account, @PathVariable int teacherId) {
        validateId("Teacher", teacherId);

        LOG.info("User {} is counting customers for teacher with id {}", account, teacherId);

        int count = validCustomerQueryService.countForTeacher(teacherId, account.partitionId());
        LOG.info("Amount of customers: {}", count);

        return count;
    }

    @RequestMapping(value = "/count", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countAll(PartitionAccount account) {
        LOG.info("User {} is counting all customers", account);

        int count = validCustomerQueryService.countAll(account.partitionId());
        LOG.info("Amount of customers: {}", count);

        return count;
    }

	// CONSTRUCTORS
	
	@Autowired
	public ValidCustomerController(ValidCustomerCommandService validCustomerCommandService,
                                   ValidCustomerQueryService validCustomerQueryService) {
		this.validCustomerCommandService = validCustomerCommandService;
        this.validCustomerQueryService = validCustomerQueryService;
	}
	
	// PRIVATE
	
	private final ValidCustomerCommandService validCustomerCommandService;
    private final ValidCustomerQueryService validCustomerQueryService;

	private static final Logger LOG = LoggerFactory.getLogger(ValidCustomerController.class);

}
