package com.superum.api.v2.lesson;

import com.superum.api.v2.Table;
import com.superum.api.v2.core.CommonControllerLogic;
import com.superum.api.v2.exception.InvalidRequestException;
import com.superum.helper.PartitionAccount;
import com.superum.helper.TimeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

/**
 * <pre>
 * API v2
 * Manages all requests for lessons
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/lesson")
public class ValidLessonController extends CommonControllerLogic {

    // COMMANDS

    @RequestMapping(method = RequestMethod.PUT, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidLessonDTO create(PartitionAccount account, @RequestBody ValidLessonDTO lesson) {
        if (lesson == null)
            throw new InvalidRequestException("Lesson cannot be null");

        LOG.info("User {} is creating a lesson: {}", account, lesson);

        ValidLessonDTO createdLesson = validLessonCommandService.create(lesson, account.partitionId());
        LOG.info("Successfully created lesson: {}", lesson);

        return createdLesson;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @RequestBody ValidLessonDTO lesson) {
        if (lesson == null)
            throw new InvalidRequestException("Lesson cannot be null");

        LOG.info("User {} is updating a lesson: {}", account, lesson);

        validLessonCommandService.update(lesson, account.partitionId());
        LOG.info("Lesson successfully updated");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(PartitionAccount account, @PathVariable int id) {
        validateId("Lesson", id);

        LOG.info("User {} is deleting a lesson with id: {}", account, id);

        validLessonCommandService.delete(id, account.partitionId());
        LOG.info("Lesson successfully deleted");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidLessonDTO read(PartitionAccount account, @PathVariable int id) {
        validateId("Lesson", id);

        LOG.info("User {} is reading lesson with id: {}", account, id);

        ValidLessonDTO lesson = validLessonQueryService.readById(id, account.partitionId());
        LOG.info("Lesson retrieved: {}", lesson);

        return lesson;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidLessonDTO> readAll(PartitionAccount account,
                                        @RequestParam(value="page", required=false) Integer page,
                                        @RequestParam(value="per_page", required=false) Integer per_page,
                                        @RequestParam(value="time_zone", required=false) String timeZone,
                                        @RequestParam(value="start_date", required=false) String startDate,
                                        @RequestParam(value="end_date", required=false) String endDate,
                                        @RequestParam(value="start", required=false) Long start,
                                        @RequestParam(value="end", required=false) Long end) {
        page = validatePage(page);
        per_page = validatePerPage(per_page);
        TimeResolver timeResolver = validateTime(timeZone, startDate, endDate, start, end);
        start = timeResolver.getStartTime();
        end = timeResolver.getEndTime();
        LOG.info("User {} is reading all lessons, from {} to {}, page {}, with {} entries per page",
                account, start, end, page, per_page);

        List<ValidLessonDTO> lessons = validLessonQueryService.readAll(page, per_page, start, end, account.partitionId());
        LOG.info("Lessons retrieved: {}", lessons);

        return lessons;
    }

    @RequestMapping(value = "/{tableName:group|teacher|customer|student}/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<ValidLessonDTO> readForTable(PartitionAccount account, @PathVariable String tableName, @PathVariable int id,
                                             @RequestParam(value="page", required=false) Integer page,
                                             @RequestParam(value="per_page", required=false) Integer per_page,
                                             @RequestParam(value="time_zone", required=false) String time_zone,
                                             @RequestParam(value="start_date", required=false) String start_date,
                                             @RequestParam(value="end_date", required=false) String end_date,
                                             @RequestParam(value="start", required=false) Long start,
                                             @RequestParam(value="end", required=false) Long end) {
        validateId(tableName, id);
        page = validatePage(page);
        per_page = validatePerPage(per_page);
        TimeResolver timeResolver = validateTime(time_zone, start_date, end_date, start, end);
        start = timeResolver.getStartTime();
        end = timeResolver.getEndTime();
        LOG.info("User {} is reading lessons for {} with id {}, from {} to {}, page {}, with {} entries per page",
                account, tableName, id, start, end, page, per_page);

        Table table = Table.forName(tableName)
                .orElseThrow(() -> new AssertionError("The regex filter should have filtered out impossible names"));

        List<ValidLessonDTO> lessons;
        switch (table) {
            case group:
                lessons = validLessonQueryService.readForGroup(id, page, per_page, start, end, account.partitionId());
                break;
            case teacher:
                lessons = validLessonQueryService.readForTeacher(id, page, per_page, start, end, account.partitionId());
                break;
            case customer:
                lessons = validLessonQueryService.readForCustomer(id, page, per_page, start, end, account.partitionId());
                break;
            case student:
                lessons = validLessonQueryService.readForStudent(id, page, per_page, start, end, account.partitionId());
                break;
            default:
                throw new AssertionError("The regex filter should have filtered out invalid names");
        }
        LOG.info("Lessons retrieved: {}", lessons);

        return lessons;
    }

    // CONSTRUCTORS

    @Autowired
    public ValidLessonController(ValidLessonCommandService validLessonCommandService,
                                 ValidLessonQueryService validLessonQueryService) {
        this.validLessonCommandService = validLessonCommandService;
        this.validLessonQueryService = validLessonQueryService;
    }

    // PRIVATE

    private final ValidLessonCommandService validLessonCommandService;
    private final ValidLessonQueryService validLessonQueryService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidLessonController.class);

}
