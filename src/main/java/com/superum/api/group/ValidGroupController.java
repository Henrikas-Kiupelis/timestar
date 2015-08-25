package com.superum.api.group;

import com.superum.api.core.CommonControllerLogic;
import com.superum.api.exception.InvalidRequestException;
import com.superum.db.Table;
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
 * Manages all requests for groups
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/group")
public class ValidGroupController extends CommonControllerLogic {

    // COMMANDS

    @RequestMapping(method = RequestMethod.PUT, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidGroupDTO create(PartitionAccount account, @RequestBody ValidGroupDTO group) {
        if (group == null)
            throw new InvalidRequestException("Group cannot be null");

        LOG.info("User {} is creating a group: {}", account, group);

        ValidGroupDTO createdGroup = validGroupCommandService.create(group, account.partitionId());
        LOG.info("Successfully created group: {}", group);

        return createdGroup;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody ValidGroupDTO group) {
        if (group == null)
            throw new InvalidRequestException("Group cannot be null");

        LOG.info("User {} is updating a group: {}", account, group);

        validGroupCommandService.update(group, account.partitionId());
        LOG.info("Group successfully updated");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(PartitionAccount account, @PathVariable int id) {
        validateId("Group", id);

        LOG.info("User {} is deleting a group with id: {}", account, id);

        validGroupCommandService.delete(id, account.partitionId());
        LOG.info("Group successfully deleted");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidGroupDTO read(PartitionAccount account, @PathVariable int id) {
        validateId("Group", id);

        LOG.info("User {} is reading group with id: {}", account, id);

        ValidGroupDTO group = validGroupQueryService.readById(id, account.partitionId());
        LOG.info("Group retrieved: {}", group);

        return group;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidGroupDTO> readAll(PartitionAccount account,
                                       @RequestParam(value="page", required=false) Integer page,
                                       @RequestParam(value="per_page", required=false) Integer per_page) {
        page = validatePage(page);
        per_page = validatePerPage(per_page);

        LOG.info("User {} is reading all groups, page {}, with {} entries per page", account, page, per_page);

        List<ValidGroupDTO> groups = validGroupQueryService.readAll(page, per_page, account.partitionId());
        LOG.info("Groups retrieved: {}", groups);

        return groups;
    }

    @RequestMapping(value = "/{tableName:teacher|customer|student}/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidGroupDTO> readForTable(PartitionAccount account, @PathVariable String tableName, @PathVariable int id,
                                              @RequestParam(value="page", required=false) Integer page,
                                              @RequestParam(value="per_page", required=false) Integer per_page) {
        validateId(tableName, id);
        page = validatePage(page);
        per_page = validatePerPage(per_page);

        LOG.info("User {} is reading groups for {} with id {}, page {}, with {} entries per page",
                account, tableName, id, page, per_page);

        Table table = Table.forName(tableName)
                .orElseThrow(() -> new AssertionError("The regex filter should have filtered out impossible names"));

        List<ValidGroupDTO> groups;
        switch (table) {
            case teacher:
                groups = validGroupQueryService.readForTeacher(id, page, per_page, account.partitionId());
                break;
            case customer:
                groups = validGroupQueryService.readForCustomer(id, page, per_page, account.partitionId());
                break;
            case student:
                groups = validGroupQueryService.readForStudent(id, page, per_page, account.partitionId());
                break;
            default:
                throw new AssertionError("The regex filter should have filtered out invalid names");
        }
        LOG.info("Groups retrieved: {}", groups);

        return groups;
    }

    // CONSTRUCTORS

    @Autowired
    public ValidGroupController(ValidGroupCommandService validGroupCommandService,
                                ValidGroupQueryService validGroupQueryService) {
        this.validGroupCommandService = validGroupCommandService;
        this.validGroupQueryService = validGroupQueryService;
    }

    // PRIVATE

    private final ValidGroupCommandService validGroupCommandService;
    private final ValidGroupQueryService validGroupQueryService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidGroupController.class);

}
