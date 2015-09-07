package com.superum.api.student;

import com.superum.api.core.CommonControllerLogic;
import com.superum.api.exception.InvalidRequestException;
import com.superum.db.Table;
import com.superum.helper.PartitionAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

/**
 * <pre>
 * API v2
 * Manages all requests for students
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/student")
public class ValidStudentController extends CommonControllerLogic {

    // COMMANDS

    @RequestMapping(method = RequestMethod.PUT, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidStudentDTO create(PartitionAccount account, @RequestBody ValidStudentDTO student) {
        if (student == null)
            throw new InvalidRequestException("Student cannot be null");

        LOG.info("User {} is creating student {}", account, student);

        ValidStudentDTO createdStudent = validStudentCommandService.create(student, account.partitionId());
        LOG.info("Successfully created student: {}", createdStudent);

        return createdStudent;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody ValidStudentDTO student) {
        if (student == null)
            throw new InvalidRequestException("Student cannot be null");

        LOG.info("User {} is updating student {}", account, student);

        validStudentCommandService.update(student, account.partitionId());
        LOG.info("Student successfully updated");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(PartitionAccount account, @PathVariable int id) {
        validateId("Student", id);

        LOG.info("User {} is deleting student with id {}", account, id);

        validStudentCommandService.delete(id, account.partitionId());
        LOG.info("Student successfully deleted");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidStudentDTO read(PartitionAccount account, @PathVariable int id) {
        validateId("Student", id);

        LOG.info("User {} is reading student with id {}", account, id);

        ValidStudentDTO student = validStudentQueryService.readById(id, account.partitionId());
        LOG.info("Student retrieved: {}", student);

        return student;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidStudentDTO> readAll(PartitionAccount account,
                                         @RequestParam(value="page", required=false) Integer page,
                                         @RequestParam(value="per_page", required=false) Integer per_page) {
        page = validatePage(page);
        per_page = validatePerPage(per_page);

        LOG.info("User {} is reading all students, page {}, with {} entries per page",
                account, page, per_page);

        List<ValidStudentDTO> students = validStudentQueryService.readAll(page, per_page, account.partitionId());
        LOG.info("Students retrieved: {}", students);

        return students;
    }

    @RequestMapping(value = "/{tableName:group|lesson|customer}/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidStudentDTO> readForTable(PartitionAccount account, @PathVariable String tableName, @PathVariable long id,
                                                 @RequestParam(value="page", required=false) Integer page,
                                                 @RequestParam(value="per_page", required=false) Integer per_page) {
        validateId(tableName, id);
        page = validatePage(page);
        per_page = validatePerPage(per_page);

        LOG.info("User {} is reading students for teacher with id {}, page {}, with {} entries per page",
                account, id, page, per_page);

        Table table = Table.forName(tableName)
                .orElseThrow(() -> new AssertionError("The regex filter should have filtered out impossible names"));

        List<ValidStudentDTO> students;
        switch (table) {
            case group:
                students = validStudentQueryService.readForGroup((int)id, page, per_page, account.partitionId());
                break;
            case lesson:
                students = validStudentQueryService.readForLesson(id, page, per_page, account.partitionId());
                break;
            case customer:
                students = validStudentQueryService.readForCustomer((int)id, page, per_page, account.partitionId());
                break;
            default:
                throw new AssertionError("The regex filter should have filtered out invalid names");
        }
        LOG.info("Students retrieved: {}", students);

        return students;
    }

    // CONSTRUCTORS

    @Autowired
    public ValidStudentController(ValidStudentCommandService validStudentCommandService, ValidStudentQueryService validStudentQueryService) {
        this.validStudentCommandService = validStudentCommandService;
        this.validStudentQueryService = validStudentQueryService;
    }

    // PRIVATE

    private final ValidStudentCommandService validStudentCommandService;
    private final ValidStudentQueryService validStudentQueryService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidStudentController.class);

}
