package com.superum.api.v2.teacher;

import com.superum.api.v2.core.CommonControllerLogic;
import com.superum.api.v2.exception.InvalidRequestException;
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
 * Manages all requests for teachers
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/teacher")
public class ValidTeacherController extends CommonControllerLogic {

    // COMMANDS

    @RequestMapping(method = POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTeacherDTO create(PartitionAccount account, @RequestBody FullTeacherDTO teacher) {
        if (teacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        LOG.info("User {} is creating teacher {}", account, teacher);

        FullTeacherDTO createdTeacher = validTeacherCommandService.create(teacher, account);
        LOG.info("Successfully created teacher: {}", createdTeacher);

        return createdTeacher;
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody FullTeacherDTO teacher) {
        if (teacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        LOG.info("User {} is updating teacher {}", account, teacher);

        validTeacherCommandService.update(teacher, account.partitionId());
        LOG.info("Successfully updated teacher");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    @ResponseBody
    public void delete(PartitionAccount account, @PathVariable int id) {
        validateId("Teacher", id);

        LOG.info("User {} is deleting teacher with id {}", account, id);

        validTeacherCommandService.delete(id, account);
        LOG.info("Successfully deleted teacher");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTeacherDTO read(PartitionAccount account, @PathVariable int id) {
        validateId("Teacher", id);

        LOG.info("User {} is reading teacher with id {}", account, id);

        FullTeacherDTO teacher = validTeacherQueryService.readById(id, account.partitionId());
        LOG.info("Teacher retrieved: {}", teacher);

        return teacher;
    }



    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FullTeacherDTO> readAll(PartitionAccount account,
                                               @RequestParam(value="page", required=false) Integer page,
                                               @RequestParam(value="per_page", required=false) Integer per_page) {

        page = validatePage(page);
        per_page = validatePerPage(per_page);

        LOG.info("User {} is reading all teachers, page {}, with {} entries per page",
                account, page, per_page);

        List<FullTeacherDTO> teachers = validTeacherQueryService.readAll(page, per_page, account.partitionId());
        LOG.info("Teachers retrieved: {}", teachers);

        return teachers;
    }

    @RequestMapping(value = "/count", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countAll(PartitionAccount account) {
        LOG.info("User {} is counting teachers", account);

        int count = validTeacherQueryService.countAll(account.partitionId());
        LOG.info("Amount of teachers: {}", count);

        return count;
    }

    // CONSTRUCTORS

    @Autowired
    public ValidTeacherController(ValidTeacherCommandService validTeacherCommandService, ValidTeacherQueryService validTeacherQueryService) {
        this.validTeacherCommandService = validTeacherCommandService;
        this.validTeacherQueryService = validTeacherQueryService;
    }

    // PRIVATE

    private final ValidTeacherCommandService validTeacherCommandService;
    private final ValidTeacherQueryService validTeacherQueryService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidTeacherController.class);

}
