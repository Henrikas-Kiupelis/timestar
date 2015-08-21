package com.superum.api.table;

import com.superum.api.core.CommonControllerLogic;
import com.superum.api.exception.InvalidRequestException;
import com.superum.api.table.dto.OptimizedLessonTableDTO;
import com.superum.helper.PartitionAccount;
import com.superum.helper.time.TimeResolver;
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
    public OptimizedLessonTableDTO getLessonTable(PartitionAccount account,
                                      @RequestParam(value = "per_page", required = false) Integer per_page,
                                      @RequestParam(value = "time_zone", required = false) String time_zone,
                                      @RequestParam(value = "start_date", required = false) String start_date,
                                      @RequestParam(value = "end_date", required = false) String end_date,
                                      @RequestParam(value = "start", required = false) Long start,
                                      @RequestParam(value = "end", required = false) Long end) {
        return getLessonTable(account, DEFAULT_HOME_PAGE, per_page, time_zone, start_date, end_date, start, end);
    }

    @RequestMapping(value = "/{page:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public OptimizedLessonTableDTO getLessonTable(PartitionAccount account, @PathVariable int page,
                                      @RequestParam(value = "per_page", required = false) Integer per_page,
                                      @RequestParam(value = "time_zone", required = false) String time_zone,
                                      @RequestParam(value = "start_date", required = false) String start_date,
                                      @RequestParam(value = "end_date", required = false) String end_date,
                                      @RequestParam(value = "start", required = false) Long start,
                                      @RequestParam(value = "end", required = false) Long end) {
        page = validatePage(page);
        per_page = validatePerPage(per_page);

        TimeResolver timeResolver = TimeResolver.from(time_zone, start_date, end_date, start, end);
        return optimizedLessonTableService.getLessonTable(page, per_page,
                timeResolver.getStartTime(), timeResolver.getEndTime(), account.partitionId());
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

}
