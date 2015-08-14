package com.superum.api.group;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.Table;
import com.superum.helper.PartitionAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api/v2/group")
public class ValidGroupController {

    // COMMANDS

    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidGroupDTO create(PartitionAccount account, @RequestBody ValidGroupDTO group) {
        if (group == null)
            throw new InvalidRequestException("Group cannot be null");

        LOG.info("User {} is creating a group: {}", account, group);

        ValidGroupDTO createdGroup = validGroupCommandService.create(group, account.partitionId());
        LOG.info("Group {} successfully created", group);

        return createdGroup;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody ValidGroupDTO group) {
        if (group == null)
            throw new InvalidRequestException("Group cannot be null");

        LOG.info("User {} is updating a group: {}", account, group);

        validGroupCommandService.update(group, account.partitionId());
        LOG.info("Group successfully updated");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
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

        ValidGroupDTO group = validGroupQueryService.readById(id, account.partitionId())
                .orElseThrow(() -> new GroupNotFoundException("Couldn't find group with id " + id));
        LOG.info("Group retrieved: {}", group);

        return group;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
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

    @RequestMapping(value = "/{tableName:teacher|customer}/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidGroupDTO> readForTeacher(PartitionAccount account, @PathVariable String tableName, @PathVariable int id,
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

    private static void validateId(String source, int id) {
        if (id <= 0)
            throw new InvalidRequestException(source + " id must be positive, not: " + id);
    }

    /**
     * <pre>
     * Defaults are set up below rather than in annotation because otherwise
     *
     *      ...?page=&per_page=
     *
     * would cause a NumberFormatException instead of using default values; this is fixed by:
     * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
     *    fail to parse an empty string;
     * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
     *    and would instead propagate down;
     * </pre>
     */
    private static int validatePage(Integer page) {
        if (page == null)
            return DEFAULT_PAGE;

        if (page <= 0)
            throw new InvalidRequestException("Pages can only be positive, not " + page);

        return page - 1; //Pages start with 1 in the URL, but start with 0 in the app logic
    }

    /**
     * <pre>
     * Defaults are set up below rather than in annotation because otherwise
     *
     *      ...?page=&per_page=
     *
     * would cause a NumberFormatException instead of using default values; this is fixed by:
     * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
     *    fail to parse an empty string;
     * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
     *    and would instead propagate down;
     * </pre>
     */
    private static int validatePerPage(Integer per_page) {
        if (per_page == null)
            return DEFAULT_PER_PAGE;

        if (per_page <= 0 || per_page > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + per_page);

        return per_page;
    }

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PER_PAGE = 25;

    private static final Logger LOG = LoggerFactory.getLogger(ValidGroupController.class);

}
