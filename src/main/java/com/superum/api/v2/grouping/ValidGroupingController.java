package com.superum.api.v2.grouping;

import com.superum.api.v2.core.CommonControllerLogic;
import com.superum.api.v2.exception.InvalidRequestException;
import com.superum.helper.PartitionAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * <pre>
 * API v2
 * Manages all requests for grouping students into groups
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/grouping")
public class ValidGroupingController extends CommonControllerLogic {

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void create(PartitionAccount account, @RequestBody ValidGroupingDTO grouping) {
        if (grouping == null)
            throw new InvalidRequestException("Grouping cannot be null");

        LOG.info("User {} is creating grouping {}", account, grouping);

        validGroupingCommandService.create(grouping, account.partitionId());
        LOG.info("Grouping successfully created");
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody ValidGroupingDTO grouping) {
        if (grouping == null)
            throw new InvalidRequestException("Grouping cannot be null");

        LOG.info("User {} is updating grouping {}", account, grouping);

        validGroupingCommandService.update(grouping, account.partitionId());
        LOG.info("Grouping successfully updated");
    }

    @RequestMapping(value = "/{field:group|student}/{id:[\\d]+}", method = DELETE)
    @ResponseBody
    public void deleteCustomer(PartitionAccount account, @PathVariable String field, @PathVariable int id) {
        validateId(field, id);

        LOG.info("User {} is deleting groupings for {} with id {}", account, field, id);

        switch (field) {
            case "group":
                validGroupingCommandService.deleteForGroup(id, account.partitionId());
                break;
            case "student":
                validGroupingCommandService.deleteForStudent(id, account.partitionId());
                break;
            default:
                throw new AssertionError("The regex filter should have filtered out invalid names");
        }
        LOG.info("Groupings successfully deleted");
    }

    // CONSTRCUTORS

    @Autowired
    public ValidGroupingController(ValidGroupingCommandService validGroupingCommandService) {
        this.validGroupingCommandService = validGroupingCommandService;
    }

    // PRIVATE

    private final ValidGroupingCommandService validGroupingCommandService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidGroupingController.class);

}
