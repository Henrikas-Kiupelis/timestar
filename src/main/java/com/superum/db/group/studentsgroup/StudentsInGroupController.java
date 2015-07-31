package com.superum.db.group.studentsgroup;

import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.superum.utils.ControllerUtils.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class StudentsInGroupController {

    @RequestMapping(value = "/group/student/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup addStudentsToGroup(Principal user, @RequestBody @Valid StudentsInGroup studentsInGroup) {
        int partitionId = PrincipalUtils.partitionId(user);
        return studentsInGroupService.addStudentsToGroup(studentsInGroup, partitionId);
    }

    @RequestMapping(value = "/group/student/{groupId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup getStudentsInGroup(Principal user, @PathVariable int groupId) {
        int partitionId = PrincipalUtils.partitionId(user);
        return studentsInGroupService.getStudentsInGroup(groupId, partitionId);
    }

    @RequestMapping(value = "/group/student/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup updateStudentsInGroup(Principal user, @RequestBody @Valid StudentsInGroup studentsInGroup) {
        int partitionId = PrincipalUtils.partitionId(user);
        return studentsInGroupService.updateStudentsInGroup(studentsInGroup, partitionId);
    }

    @RequestMapping(value = "/group/student/delete/{groupId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup deleteStudentsInGroup(Principal user, @PathVariable int groupId) {
        int partitionId = PrincipalUtils.partitionId(user);
        return studentsInGroupService.deleteStudentsInGroup(groupId, partitionId);
    }

    @RequestMapping(value = "/group/student/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup deleteStudentsInGroup(Principal user, @RequestBody @Valid StudentsInGroup studentsInGroup) {
        int partitionId = PrincipalUtils.partitionId(user);
        return studentsInGroupService.deleteStudentsInGroup(studentsInGroup, partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public StudentsInGroupController(StudentsInGroupService studentsInGroupService) {
        this.studentsInGroupService = studentsInGroupService;
    }

    // PRIVATE

    private final StudentsInGroupService studentsInGroupService;

}
