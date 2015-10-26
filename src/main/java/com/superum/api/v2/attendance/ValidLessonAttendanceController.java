package com.superum.api.v2.attendance;

import com.superum.api.v2.core.CommonControllerLogic;
import com.superum.api.v2.exception.InvalidRequestException;
import com.superum.helper.PartitionAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * <pre>
 * API v2
 * Manages all requests for lesson attendance
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/lesson/attendance")
public class ValidLessonAttendanceController extends CommonControllerLogic {

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void create(PartitionAccount account, @RequestBody ValidLessonAttendanceDTO lessonAttendance) {
        if (lessonAttendance == null)
            throw new InvalidRequestException("Lesson attendance cannot be null");

        LOG.info("User {} is creating lesson attendance {}", account, lessonAttendance);

        validLessonAttendanceCommandService.create(lessonAttendance, account.partitionId());
        LOG.info("Lesson attendance successfully created");
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody ValidLessonAttendanceDTO lessonAttendance) {
        if (lessonAttendance == null)
            throw new InvalidRequestException("Lesson attendance cannot be null");

        LOG.info("User {} is updating lesson attendance {}", account, lessonAttendance);

        validLessonAttendanceCommandService.update(lessonAttendance, account.partitionId());
        LOG.info("Lesson attendance successfully updated");
    }

    @RequestMapping(value = "/{field:lesson|student}/{id:[\\d]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteCustomer(PartitionAccount account, @PathVariable String field, @PathVariable long id) {
        validateId(field, id);

        LOG.info("User {} is deleting lesson attendance for {} with id {}", account, field, id);

        switch (field) {
            case "lesson":
                validLessonAttendanceCommandService.deleteForLesson(id, account.partitionId());
                break;
            case "student":
                validLessonAttendanceCommandService.deleteForStudent((int)id, account.partitionId());
                break;
            default:
                throw new AssertionError("The regex filter should have filtered out invalid names");
        }
        LOG.info("Lesson attendance successfully deleted");
    }

    // CONSTRCUTORS

    @Autowired
    public ValidLessonAttendanceController(ValidLessonAttendanceCommandService validLessonAttendanceCommandService) {
        this.validLessonAttendanceCommandService = validLessonAttendanceCommandService;
    }

    // PRIVATE

    private final ValidLessonAttendanceCommandService validLessonAttendanceCommandService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidLessonAttendanceController.class);

}
