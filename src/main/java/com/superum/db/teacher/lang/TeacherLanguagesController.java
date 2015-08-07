package com.superum.db.teacher.lang;

import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherLanguagesController {

	@RequestMapping(value = "/teacher/lang/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages addLanguagesToTeacher(Principal user, @RequestBody @Valid TeacherLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.addLanguagesToTeacher(languages, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages getLanguagesForTeacher(Principal user, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.getLanguagesForTeacher(teacherId, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages updateLanguagesForTeacher(Principal user, @RequestBody @Valid TeacherLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.updateLanguagesForTeacher(languages, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/delete/{teacherId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages deleteLanguagesForTeacher(Principal user, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.deleteLanguagesForTeacher(teacherId, partitionId);
	}
	
	@RequestMapping(value = "/teacher/lang/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
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
