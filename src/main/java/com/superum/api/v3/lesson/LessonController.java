package com.superum.api.v3.lesson;

import com.superum.api.v2.Table;
import com.superum.api.v2.core.CommonControllerLogic;
import com.superum.api.v2.exception.InvalidRequestException;
import com.superum.helper.PartitionAccount;
import com.superum.helper.TimeResolver;
import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/timestar/api/v3/lesson")
public class LessonController extends CommonControllerLogic {

    // COMMANDS

    @RequestMapping(method = POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FetchedLesson create(PartitionAccount account, @RequestBody SuppliedLesson lesson) {
        Null.check(lesson).ifAny(() -> new InvalidRequestException("Lesson cannot be null"));
        LOG.info("User {} is creating a lesson: {}", account, lesson);

        SuppliedLessonWithTimestamp suppliedLesson = lessonTransformer.from(lesson, true);
        FetchedLesson createdLesson = lessonCommands.create(suppliedLesson, account.partitionId());
        LOG.info("Successfully created lesson: {}", lesson);

        return createdLesson;
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = PUT, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(PartitionAccount account, @PathVariable long id, @RequestBody SuppliedLesson lesson) {
        Null.check(lesson).ifAny(() -> new InvalidRequestException("Lesson cannot be null"));
        validateId("Lesson", id);
        LOG.info("User {} is updating a lesson: {}", account, lesson);

        SuppliedLessonWithTimestamp suppliedLesson = lessonTransformer.from(lesson, false);
        lessonCommands.update(id, suppliedLesson, account.partitionId());
        LOG.info("Lesson successfully updated");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    @ResponseBody
    public void delete(PartitionAccount account, @PathVariable long id) {
        validateId("Lesson", id);
        LOG.info("User {} is deleting a lesson with id: {}", account, id);

        lessonCommands.delete(id, account.partitionId());
        LOG.info("Lesson successfully deleted");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FetchedLesson read(PartitionAccount account, @PathVariable long id) {
        validateId("Lesson", id);
        LOG.info("User {} is reading lesson with id: {}", account, id);

        FetchedLesson lesson = lessonQueries.readById(id);
        LOG.info("Lesson retrieved: {}", lesson);

        return lesson;
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FetchedLesson> readAll(PartitionAccount account,
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

        List<FetchedLesson> lessons = lessonQueries.readAll(page, per_page, start, end);
        LOG.info("Lessons retrieved: {}", lessons);

        return lessons;
    }

    @RequestMapping(value = "/{tableName:group|teacher|customer|student}/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FetchedLesson> readForTable(PartitionAccount account, @PathVariable String tableName, @PathVariable int id,
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

        List<FetchedLesson> lessons;
        switch (table) {
            case group:
                lessons = lessonQueries.readForGroup(id, page, per_page, start, end);
                break;
            case teacher:
                lessons = lessonQueries.readForTeacher(id, page, per_page, start, end);
                break;
            case customer:
                lessons = lessonQueries.readForCustomer(id, page, per_page, start, end);
                break;
            case student:
                lessons = lessonQueries.readForStudent(id, page, per_page, start, end);
                break;
            default:
                throw new AssertionError("The regex filter should have filtered out invalid names");
        }
        LOG.info("Lessons retrieved: {}", lessons);

        return lessons;
    }

    // CONSTRUCTORS

    @Autowired
    public LessonController(LessonCommands lessonCommands, LessonQueries lessonQueries,
                            LessonTransformer lessonTransformer) {
        this.lessonCommands = lessonCommands;
        this.lessonQueries = lessonQueries;
        this.lessonTransformer = lessonTransformer;
    }

    // PRIVATE

    private final LessonCommands lessonCommands;
    private final LessonQueries lessonQueries;
    private final LessonTransformer lessonTransformer;

    private static final Logger LOG = LoggerFactory.getLogger(LessonController.class);
}
