package com.superum.api.student;

import com.superum.db.group.student.Student;
import com.superum.helper.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.superum.helper.utils.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api/v2/student")
public class FullStudentController {

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<Student> readAllStudents(Principal user) {
        int partitionId = PrincipalUtils.partitionId(user);
        return fullStudentService.readAllStudents(partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public FullStudentController(FullStudentService fullStudentService) {
        this.fullStudentService = fullStudentService;
    }

    // PRIVATE

    private final FullStudentService fullStudentService;


}
