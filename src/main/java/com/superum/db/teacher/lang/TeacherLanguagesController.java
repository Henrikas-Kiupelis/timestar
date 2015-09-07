package com.superum.db.teacher.lang;

import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherLanguagesController {

	@RequestMapping(value = "/teacher/lang/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages addLanguagesToTeacher(PartitionAccount account, @RequestBody @Valid TeacherLanguages languages) {
		return languageService.addLanguagesToTeacher(languages, account.partitionId());
	}
	
	@RequestMapping(value = "/teacher/lang/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages getLanguagesForTeacher(PartitionAccount account, @PathVariable int teacherId) {
		return languageService.getLanguagesForTeacher(teacherId, account.partitionId());
	}
	
	@RequestMapping(value = "/teacher/lang/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages updateLanguagesForTeacher(PartitionAccount account, @RequestBody @Valid TeacherLanguages languages) {
		return languageService.updateLanguagesForTeacher(languages, account.partitionId());
	}
	
	@RequestMapping(value = "/teacher/lang/delete/{teacherId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages deleteLanguagesForTeacher(PartitionAccount account, @PathVariable int teacherId) {
		return languageService.deleteLanguagesForTeacher(teacherId, account.partitionId());
	}
	
	@RequestMapping(value = "/teacher/lang/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public TeacherLanguages deleteLanguagesForTeacher(PartitionAccount account, @RequestBody @Valid TeacherLanguages languages) {
		return languageService.deleteLanguagesForTeacher(languages, account.partitionId());
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherLanguagesController(TeacherLanguagesService languageService) {
		this.languageService = languageService;
	}

	// PRIVATE
	
	private final TeacherLanguagesService languageService;

}
