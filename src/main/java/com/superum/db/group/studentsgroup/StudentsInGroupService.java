package com.superum.db.group.studentsgroup;

import org.springframework.stereotype.Service;

@Service
public interface StudentsInGroupService {

    StudentsInGroup addStudentsToGroup(StudentsInGroup studentsInGroup, int partitionId);

    StudentsInGroup getStudentsInGroup(int groupId, int partitionId);

    StudentsInGroup updateStudentsInGroup(StudentsInGroup studentsInGroup, int partitionId);

    StudentsInGroup deleteStudentsInGroup(int groupId, int partitionId);

    StudentsInGroup deleteStudentsInGroup(StudentsInGroup studentsInGroup, int partitionId);

}
