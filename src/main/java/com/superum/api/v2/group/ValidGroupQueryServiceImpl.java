package com.superum.api.v2.group;

import com.superum.api.v2.teacher.TeacherNotFoundException;
import com.superum.helper.jooq.DefaultQueries;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.CustomerRecord;
import timestar_v2.tables.records.GroupOfStudentsRecord;
import timestar_v2.tables.records.StudentRecord;
import timestar_v2.tables.records.TeacherRecord;

import java.util.List;

import static timestar_v2.Tables.GROUP_OF_STUDENTS;

@Service
public class ValidGroupQueryServiceImpl implements ValidGroupQueryService {

    @Override
    public ValidGroupDTO readById(int groupId, int partitionId) {
        return defaultGroupQueries.read(groupId, partitionId, ValidGroupDTO::valueOf)
                .orElseThrow(() -> new GroupNotFoundException("Couldn't find group with id " + groupId));
    }

    @Override
    public List<ValidGroupDTO> readAll(int page, int amount, int partitionId) {
        return defaultGroupQueries.readAll(page, amount, partitionId, ValidGroupDTO::valueOf);
    }

    @Override
    public List<ValidGroupDTO> readForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return defaultGroupQueries.readForForeignKey(page, amount, partitionId,
                GROUP_OF_STUDENTS.TEACHER_ID, teacherId, ValidGroupDTO::valueOf);
    }

    @Override
    public List<ValidGroupDTO> readForCustomer(int customerId, int page, int amount, int partitionId) {
        if (!defaultCustomerQueries.exists(customerId, partitionId))
            throw new TeacherNotFoundException("No customer with given id exists: " + customerId);

        return defaultGroupQueries.readForForeignKey(page, amount, partitionId,
                GROUP_OF_STUDENTS.CUSTOMER_ID, customerId, ValidGroupDTO::valueOf);
    }

    @Override
    public List<ValidGroupDTO> readForStudent(int studentId, int page, int amount, int partitionId) {
        if (!defaultStudentQueries.exists(studentId, partitionId))
            throw new TeacherNotFoundException("No student with given id exists: " + studentId);

        return groupsForStudent.fetch(studentId, page, amount, partitionId);
    }

    @Override
    public List<ValidGroupDTO> readWithoutCustomer(int page, int amount, int partitionId) {
        Condition condition = defaultGroupQueries.partitionId(partitionId)
                .and(GROUP_OF_STUDENTS.CUSTOMER_ID.isNull());

        return defaultGroupQueries.readForCondition(page, amount, condition, ValidGroupDTO::valueOf);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidGroupQueryServiceImpl(GroupsForStudentFetcher groupsForStudent,
                                      DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries,
                                      DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries,
                                      DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries,
                                      DefaultQueries<StudentRecord, Integer> defaultStudentQueries) {
        this.groupsForStudent = groupsForStudent;
        this.defaultGroupQueries = defaultGroupQueries;
        this.defaultTeacherQueries = defaultTeacherQueries;
        this.defaultCustomerQueries = defaultCustomerQueries;
        this.defaultStudentQueries = defaultStudentQueries;
    }

    // PRIVATE

    private final GroupsForStudentFetcher groupsForStudent;
    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;
    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;
    private final DefaultQueries<StudentRecord, Integer> defaultStudentQueries;

}
