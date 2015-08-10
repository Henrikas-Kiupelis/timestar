package com.superum.db.lesson.table;

import com.superum.db.lesson.table.core.LessonTable;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@Controller
@RequestMapping(value = "/timestar/api")
public class LessonTableController {

	@RequestMapping(value = "/lesson/table", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonTable showLessonData(PartitionAccount account,
									  @RequestParam(value="per_page", required=false) Integer amount,
									  @RequestParam(value="start", required=false) Date start,
									  @RequestParam(value="end", required=false) Date end) {
		if (amount == null)
			amount = DEFAULT_PAGE_AMOUNT;
		
		if (start == null)
			start = DateUtils.sqlNow();
		
		if (end == null)
			end = DateUtils.sqlNow();
		
		return lessonTableService.lessonData(amount, 0, start, end, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/table/{pageId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonTable showLessonData(PartitionAccount account, @PathVariable int pageId,
									  @RequestParam(value="per_page", required=false) Integer amount,
									  @RequestParam(value="start", required=false) Date start,
									  @RequestParam(value="end", required=false) Date end) {
		if (amount == null)
			amount = DEFAULT_PAGE_AMOUNT;
		
		if (start == null)
			start = DateUtils.sqlNow();
		
		if (end == null)
			end = DateUtils.sqlNow();
		
		return lessonTableService.lessonData(amount, pageId * amount, start, end, account.partitionId());
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
