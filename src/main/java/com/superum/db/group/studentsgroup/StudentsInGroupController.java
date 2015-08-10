package com.superum.db.group.studentsgroup;

import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class StudentsInGroupController {

    @RequestMapping(value = "/group/student/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup addStudentsToGroup(PartitionAccount account, @RequestBody @Valid StudentsInGroup studentsInGroup) {
        return studentsInGroupService.addStudentsToGroup(studentsInGroup, account.partitionId());
    }

    @RequestMapping(value = "/group/student/{groupId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup getStudentsInGroup(PartitionAccount account, @PathVariable int groupId) {
        return studentsInGroupService.getStudentsInGroup(groupId, account.partitionId());
    }

    @RequestMapping(value = "/group/student/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup updateStudentsInGroup(PartitionAccount account, @RequestBody @Valid StudentsInGroup studentsInGroup) {
        return studentsInGroupService.updateStudentsInGroup(studentsInGroup, account.partitionId());
    }

    @RequestMapping(value = "/group/student/delete/{groupId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup deleteStudentsInGroup(PartitionAccount account, @PathVariable int groupId) {
        return studentsInGroupService.deleteStudentsInGroup(groupId, account.partitionId());
    }

    @RequestMapping(value = "/group/student/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public StudentsInGroup deleteStudentsInGroup(PartitionAccount account, @RequestBody @Valid StudentsInGroup studentsInGroup) {
        return studentsInGroupService.deleteStudentsInGroup(studentsInGroup, account.partitionId());
    }

    // CONSTRUCTORS

    @Autowired
    public StudentsInGroupController(StudentsInGroupService studentsInGroupService) {
        this.studentsInGroupService = studentsInGroupService;
    }

    // PRIVATE

    private final StudentsInGroupService studentsInGroupService;

}
