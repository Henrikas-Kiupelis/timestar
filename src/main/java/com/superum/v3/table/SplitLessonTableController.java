package com.superum.v3.table;

import com.superum.api.core.CommonControllerLogic;
import com.superum.api.exception.InvalidRequestException;
import com.superum.api.teacher.ValidTeacherController;
import com.superum.helper.PartitionAccount;
import com.superum.helper.time.TimeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;
import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * API v3
 * Manages all requests for lesson table
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@Controller
@RequestMapping(value = "/timestar/api/v3/lesson/table")
public class SplitLessonTableController extends CommonControllerLogic {

    @RequestMapping(value = "/size", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int getTeacherCount(PartitionAccount account) {
        return validTeacherController.countAll(account);
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public Table getTableData(PartitionAccount account,
                            @RequestParam(value = "per_page", required = false) Integer per_page,
                            @RequestParam(value = "time_zone", required = false) String time_zone,
                            @RequestParam(value = "start_date", required = false) String start_date,
                            @RequestParam(value = "end_date", required = false) String end_date,
                            @RequestParam(value = "start", required = false) Long start,
                            @RequestParam(value = "end", required = false) Long end) {
        return getTableData(account, DEFAULT_HOME_PAGE, per_page, time_zone, start_date, end_date, start, end);
    }

    @RequestMapping(value = "/data/{page:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public Table getTableData(PartitionAccount account, @PathVariable int page,
                            @RequestParam(value = "per_page", required = false) Integer per_page,
                            @RequestParam(value = "time_zone", required = false) String time_zone,
                            @RequestParam(value = "start_date", required = false) String start_date,
                            @RequestParam(value = "end_date", required = false) String end_date,
                            @RequestParam(value = "start", required = false) Long start,
                            @RequestParam(value = "end", required = false) Long end) {
        page = validatePage(page);
        per_page = validatePerPage(per_page);
        TimeResolver timeResolver = validateTime(time_zone, start_date, end_date, start, end);
        start = timeResolver.getStartTime();
        end = timeResolver.getEndTime();
        LOG.info("User {} is reading lesson table data, from {} to {}, page {}, with {} entries per page",
                account, start, end, page, per_page);

        Table lessonTable = splitLessonTableQueryService.getLessonTable(page, per_page, start, end, account.partitionId());
        LOG.info("Table successfully read; please enable DEBUG logging to see its contents");
        LOG.debug("Table contents: {}", lessonTable);

        return lessonTable;
    }

    @RequestMapping(value = "/report/{source:teacher|customer}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<TableReport> getReportData(PartitionAccount account, @PathVariable String source,
                             @RequestParam(value = "id") String id) {
        validate(id).not().Null().not().blank().commaSeparatedListOfIntegers()
                .ifInvalid(() -> new InvalidRequestException("Parameter id must be a comma separated list of " +
                        "positive integers, not: " + id));

        LOG.info("User {} is reading reports for {}s with ids: {}", account, source, id);

        List<Integer> ids = Stream.of(id.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<TableReport> reports;
        switch (source) {
            case "teacher":
                reports = splitLessonTableQueryService.teacherReport(ids, account.partitionId());
                break;
            case "customer":
                reports = splitLessonTableQueryService.customerReport(ids, account.partitionId());
                break;
            default:
                throw new AssertionError("The regex filter should have filtered out invalid names");
        }
        LOG.info("Reports successfully done; please enable DEBUG logging to see contents");
        LOG.debug("Reports: {}", reports);

        return reports;
    }

    @RequestMapping(value = "/data/full", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTable getTableDataFull(PartitionAccount account,
                                  @RequestParam(value = "per_page", required = false) Integer per_page,
                                  @RequestParam(value = "time_zone", required = false) String time_zone,
                                  @RequestParam(value = "start_date", required = false) String start_date,
                                  @RequestParam(value = "end_date", required = false) String end_date,
                                  @RequestParam(value = "start", required = false) Long start,
                                  @RequestParam(value = "end", required = false) Long end) {
        return getTableDataFull(account, DEFAULT_HOME_PAGE, per_page, time_zone, start_date, end_date, start, end);
    }

    @RequestMapping(value = "/data/full/{page:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FullTable getTableDataFull(PartitionAccount account, @PathVariable int page,
                                  @RequestParam(value = "per_page", required = false) Integer per_page,
                                  @RequestParam(value = "time_zone", required = false) String time_zone,
                                  @RequestParam(value = "start_date", required = false) String start_date,
                                  @RequestParam(value = "end_date", required = false) String end_date,
                                  @RequestParam(value = "start", required = false) Long start,
                                  @RequestParam(value = "end", required = false) Long end) {
        page = validatePage(page);
        per_page = validatePerPage(per_page);
        TimeResolver timeResolver = validateTime(time_zone, start_date, end_date, start, end);
        start = timeResolver.getStartTime();
        end = timeResolver.getEndTime();
        LOG.info("User {} is reading lesson table data, from {} to {}, page {}, with {} entries per page",
                account, start, end, page, per_page);

        FullTable lessonTable = splitLessonTableQueryService.getLessonTableFull(page, per_page, start, end, account.partitionId());
        LOG.info("Table successfully read; please enable DEBUG logging to see its contents");
        LOG.debug("Table contents: {}", lessonTable);

        return lessonTable;
    }

    // CONSTRUCTORS

    @Autowired
    public SplitLessonTableController(ValidTeacherController validTeacherController,
                                      SplitLessonTableQueryService splitLessonTableQueryService) {
        this.validTeacherController = validTeacherController;
        this.splitLessonTableQueryService = splitLessonTableQueryService;
    }

    // PROTECTED

    @Override
    protected int validatePerPage(Integer per_page) {
        if (per_page == null)
            return DEFAULT_PER_PAGE;

        if (per_page <= 0 || per_page > 12)
            throw new InvalidRequestException("You can only request 1-12 items per page, not " + per_page);

        return per_page;
    }

    // PRIVATE

    private final ValidTeacherController validTeacherController;
    private final SplitLessonTableQueryService splitLessonTableQueryService;

    private static final int DEFAULT_HOME_PAGE = 1;
    private static final Integer DEFAULT_PER_PAGE = 6;

    private static final Logger LOG = LoggerFactory.getLogger(SplitLessonTableController.class);

}
