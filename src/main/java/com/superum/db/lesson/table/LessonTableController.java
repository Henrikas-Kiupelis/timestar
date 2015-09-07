package com.superum.db.lesson.table;

import com.superum.db.lesson.table.core.LessonTable;
import com.superum.helper.PartitionAccount;
import com.superum.helper.time.TimeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

@Controller
@RequestMapping(value = "/timestar/api")
public class LessonTableController {

	@RequestMapping(value = "/lesson/table", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonTable showLessonData(PartitionAccount account,
									  @RequestParam(value="per_page", required=false) Integer amount,
									  @RequestParam(value="timeZone", required=false) String timeZone,
									  @RequestParam(value="startDate", required=false) String startDate,
									  @RequestParam(value="endDate", required=false) String endDate,
									  @RequestParam(value="start", required=false) Long start,
									  @RequestParam(value="end", required=false) Long end) {
		if (amount == null)
			amount = DEFAULT_PAGE_AMOUNT;

        TimeResolver timeResolver = TimeResolver.from(timeZone, startDate, endDate, start, end);
		return lessonTableService.lessonData(amount, 0, timeResolver.getStartTime(), timeResolver.getEndTime(), account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/table/{pageId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonTable showLessonData(PartitionAccount account, @PathVariable int pageId,
									  @RequestParam(value="per_page", required=false) Integer amount,
                                      @RequestParam(value="timeZone", required=false) String timeZone,
                                      @RequestParam(value="startDate", required=false) String startDate,
                                      @RequestParam(value="endDate", required=false) String endDate,
                                      @RequestParam(value="start", required=false) Long start,
                                      @RequestParam(value="end", required=false) Long end) {
		if (amount == null)
			amount = DEFAULT_PAGE_AMOUNT;

        TimeResolver timeResolver = TimeResolver.from(timeZone, startDate, endDate, start, end);
		return lessonTableService.lessonData(amount, pageId * amount, timeResolver.getStartTime(), timeResolver.getEndTime(), account.partitionId());
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableController(LessonTableService lessonTableService) {
		this.lessonTableService = lessonTableService;
	}

	// PRIVATE
	
	private final LessonTableService lessonTableService;
	
	private static final Integer DEFAULT_PAGE_AMOUNT = 6;

}
