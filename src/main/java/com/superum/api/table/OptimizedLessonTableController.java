package com.superum.api.table;

import com.superum.api.core.CommonControllerLogic;
import com.superum.api.exception.InvalidRequestException;
import com.superum.api.table.dto.OptimizedLessonTableDTO;
import com.superum.helper.PartitionAccount;
import com.superum.helper.time.TimeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

/**
 * <pre>
 * API v2
 * Manages all requests for lesson table
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 * </pre>
 */
@Controller
@RequestMapping(value = "/timestar/api/v2/lesson/table")
public class OptimizedLessonTableController extends CommonControllerLogic {

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public OptimizedLessonTableDTO read(PartitionAccount account,
                                      @RequestParam(value = "per_page", required = false) Integer per_page,
                                      @RequestParam(value = "time_zone", required = false) String time_zone,
                                      @RequestParam(value = "start_date", required = false) String start_date,
                                      @RequestParam(value = "end_date", required = false) String end_date,
                                      @RequestParam(value = "start", required = false) Long start,
                                      @RequestParam(value = "end", required = false) Long end) {
        return read(account, DEFAULT_HOME_PAGE, per_page, time_zone, start_date, end_date, start, end);
    }

    @RequestMapping(value = "/{page:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public OptimizedLessonTableDTO read(PartitionAccount account, @PathVariable int page,
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
        LOG.info("User {} is reading lesson table, from {} to {}, page {}, with {} entries per page",
                account, start, end, page, per_page);

        OptimizedLessonTableDTO cached = TableCache.get(page, per_page, start, end);
        if (cached != null) {
            LOG.info("Returning cached table; please enable DEBUG logging to see its contents");
            LOG.debug("Table contents: {}", cached);

            return cached;
        }

        OptimizedLessonTableDTO lessonTable = optimizedLessonTableService.getLessonTable(page, per_page,
                start, end, account.partitionId());
        LOG.info("Table successfully read; please enable DEBUG logging to see its contents");
        LOG.debug("Table contents: {}", lessonTable);

        TableCache.put(page, per_page, start, end, lessonTable);
        return lessonTable;
    }

    // CONSTRUCTORS

    @Autowired
    public OptimizedLessonTableController(OptimizedLessonTableService optimizedLessonTableService) {
        this.optimizedLessonTableService = optimizedLessonTableService;
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

    private final OptimizedLessonTableService optimizedLessonTableService;

    private static final int DEFAULT_HOME_PAGE = 1;
    private static final Integer DEFAULT_PER_PAGE = 6;

    private static final Logger LOG = LoggerFactory.getLogger(OptimizedLessonTableController.class);

}
