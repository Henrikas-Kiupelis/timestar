package com.superum.api.teacher;

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
 * Manages all requests for Teachers
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/teacher")
public class FullTeacherController {

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTeacher createTeacher(PartitionAccount account, @RequestBody FullTeacher teacher) {
        if (teacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        LOG.info("User {} is creating FullTeacher {}", account, teacher);

        FullTeacher createdTeacher = fullTeacherService.createTeacher(teacher, account);
        LOG.info("Successfully created FullTeacher: {}", createdTeacher);

        return createdTeacher;
    }

    @RequestMapping(value = "/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTeacher readTeacher(PartitionAccount account, @PathVariable int teacherId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        LOG.info("User {} is reading FullTeacher with id {}", account, teacherId);

        FullTeacher teacher = fullTeacherService.readTeacher(teacherId, account.partitionId());
        LOG.info("Successfully read FullTeacher: {}", teacher);

        return teacher;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTeacher updateTeacher(PartitionAccount account, @RequestBody FullTeacher teacher) {
        if (teacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        LOG.info("User {} is updating FullTeacher {}", account, teacher);

        FullTeacher updatedTeacher = fullTeacherService.updateTeacher(teacher, account.partitionId());
        LOG.info("Successfully updated FullTeacher: {}", updatedTeacher);

        return updatedTeacher;
    }

    @RequestMapping(value = "/delete/{teacherId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTeacher deleteTeacher(PartitionAccount account, @PathVariable int teacherId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        LOG.info("User {} is deleting FullTeacher with id {}", account, teacherId);

        FullTeacher deletedTeacher = fullTeacherService.deleteTeacher(teacherId, account);
        LOG.info("Successfully deleted FullTeacher: {}", deletedTeacher);

        return deletedTeacher;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FullTeacher> readTeachersAll(PartitionAccount account,
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

        LOG.info("User {} is reading all FullTeachers", account);
        LOG.info("Reading page {}, with {} entries per page", page, per_page);

        List<FullTeacher> teachers = fullTeacherService.readTeachersAll(page, per_page, account.partitionId());
        LOG.info("Successfully read FullTeachers: {}", teachers);

        return teachers;
    }

    @RequestMapping(value = "/all/count", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countTeachers(PartitionAccount account) {
        LOG.info("User {} is counting FullTeachers", account);

        int count = fullTeacherService.countAll(account.partitionId());
        LOG.info("Amount of FullTeachers: {}", count);

        return count;
    }

    @RequestMapping(value = "/exists", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTeacher teacherExists(PartitionAccount account, @RequestBody FullTeacher teacher) {
        if (teacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        LOG.info("User {} is checking if teacher {} exists", account, teacher);

        FullTeacher existingTeacher = fullTeacherService.exists(teacher, account.partitionId());
        LOG.info("Teacher retrieved: {}", existingTeacher);

        return existingTeacher;
    }

    // CONSTRUCTORS

    @Autowired
    public FullTeacherController(FullTeacherService fullTeacherService) {
        this.fullTeacherService = fullTeacherService;
    }

    // PRIVATE

    private final FullTeacherService fullTeacherService;

    private Integer ensurePageNotNull(Integer page) {
        return (page == null ? DEFAULT_PAGE : page);
    }

    private Integer ensurePerPageNotNull(Integer per_page) {
        return per_page == null ? DEFAULT_PER_PAGE : per_page;
    }

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PER_PAGE = 25;

    private static final Logger LOG = LoggerFactory.getLogger(FullTeacherController.class);

}
