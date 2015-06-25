package com.superum.db.lesson.table;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.superum.db.lesson.table.core.LessonTable;

@Controller
@RequestMapping(value = "/timestar/api")
public class LessonTableController {

	@RequestMapping(value = "/lesson/table", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonTable lessonDataStart(@RequestParam(value="per_page", required=false) Integer amount,
									   @RequestParam(value="start", required=false) Date start,
									   @RequestParam(value="end", required=false) Date end) {
		if (amount == null)
			amount = DEFAULT_PAGE_AMOUNT;
		
		return lessonTableService.lessonData(amount, 0, start, end);
	}
	
	@RequestMapping(value = "/lesson/table/{pageId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonTable lessonData(@PathVariable int pageId,
								  @RequestParam(value="per_page", required=false) Integer amount,
			   					  @RequestParam(value="start", required=false) Date start,
			   					  @RequestParam(value="end", required=false) Date end) {
		if (amount == null)
			amount = DEFAULT_PAGE_AMOUNT;
		
		return lessonTableService.lessonData(amount, pageId * amount, start, end);
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
