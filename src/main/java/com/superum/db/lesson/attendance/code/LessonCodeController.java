package com.superum.db.lesson.attendance.code;

import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.superum.utils.ControllerUtils.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonCodeController {

	@RequestMapping(value = "/lesson/attendance/code/{lessonId}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public int verifyStudentCode(Principal user, @PathVariable long lessonId, @RequestParam(value="code") int code) {
		if (code > 999999 || code < 100000)
			throw new IllegalArgumentException("Code must be between 100000 and 999999");
		
		int partitionId = PrincipalUtils.partitionId(user);
		return codeService.verifyStudentId(lessonId, code, partitionId);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonCodeController(LessonCodeService codeService) {
		this.codeService = codeService;
	}

	// PRIVATE
	
	private final LessonCodeService codeService;

}
