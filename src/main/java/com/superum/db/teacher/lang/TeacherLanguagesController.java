package com.superum.db.teacher.lang;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.superum.utils.PrincipalUtils;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherLanguagesController {

	@RequestMapping(value = "/teacher/lang/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages addLanguagesToTeacher(Principal user, @RequestBody @Valid TeacherLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.addLanguagesToTeacher(languages, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages getLanguagesForTeacher(Principal user, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.getLanguagesForTeacher(teacherId, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages updateLanguagesForTeacher(Principal user, @RequestBody @Valid TeacherLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.updateLanguagesForTeacher(languages, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/delete/{teacherId:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages deleteLanguagesForTeacher(Principal user, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.deleteLanguagesForTeacher(teacherId, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages deleteLanguagesForTeacher(Principal user, @RequestBody @Valid TeacherLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.deleteLanguagesForTeacher(languages, partitionId);
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherLanguagesController(TeacherLanguagesService languageService) {
		this.languageService = languageService;
	}

	// PRIVATE
	
	private final TeacherLanguagesService languageService;

}
