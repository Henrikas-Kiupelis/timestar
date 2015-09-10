package com.superum.api.v2.student;

import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.exception.DatabaseException;
import com.superum.helper.jooq.DefaultCommands;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.ForeignQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.CustomerRecord;
import timestar_v2.tables.records.StudentRecord;

@Service
@Transactional
public class ValidStudentCommandServiceImpl implements ValidStudentCommandService {

    @Override
    public ValidStudentDTO create(ValidStudentDTO validStudentDTO, int partitionId) {
        ValidStudent student = new ValidStudent(validStudentDTO);

        if (student.hasId())
            throw new InvalidStudentException("Provided student has its id set; please unset it or use POST instead!");

        if (!student.canBeInserted())
            throw new InvalidStudentException("Provided student does not have the following mandatory fields set: "
                    + student.missingMandatoryFieldNames().join(", "));

        if (student.hasNeitherCustomerIdNorStartDate())
            throw new InvalidStudentException("Provided student has neither customerId nor startDate set; " +
                    "it must have at least one of those!");

        if (student.hasBothCustomerAndStartDate())
            throw new InvalidStudentException("Provided student has both customerId and startDate set; " +
                    "it can have only one of those!");

        if (student.hasNonExistentCustomerId(id -> !defaultCustomerQueries.exists(id, partitionId)))
            throw new CustomerNotFoundException("Couldn't find customer id for student: " + student);

        return defaultStudentCommands.create(student, partitionId, ValidStudentDTO::valueOf)
                .orElseThrow(() -> new DatabaseException("Couldn't return student after inserting it: " + student));
    }

    @Override
    public void update(ValidStudentDTO validStudentDTO, int partitionId) {
        ValidStudent student = new ValidStudent(validStudentDTO);

        if (!student.hasId())
            throw new InvalidStudentException("Provided student doesn't have its id set; please set it or use PUT instead!");

        if (!student.updateFields().findAny().isPresent())
            throw new InvalidStudentException("Provided student only has its id set; to update this student, set additional fields!");

        if (student.hasBothCustomerAndStartDate())
            throw new InvalidStudentException("Provided student has both customerId and startDate set; " +
                    "it can have only one of those!");

        if (!defaultStudentQueries.exists(student.getId(), partitionId))
            throw new StudentNotFoundException("Couldn't find student with id " + student.getId());

        if (student.hasNonExistentCustomerId(id -> !defaultCustomerQueries.exists(id, partitionId)))
            throw new CustomerNotFoundException("Couldn't find customer id for student: " + student);

        if (defaultStudentCommands.update(student, partitionId) == 0)
            throw new DatabaseException("Couldn't update student: " + student);
    }

    @Override
    public void delete(int studentId, int partitionId) {
        if (!defaultStudentQueries.exists(studentId, partitionId))
            throw new StudentNotFoundException("Couldn't find student with id " + studentId);

        if (foreignStudentQueries.isUsed(studentId))
            throw new UnsafeStudentDeleteException("Cannot delete student with id " + studentId +
                    " while it still has entries in other tables");

        if (defaultStudentCommands.delete(studentId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete student with id: " + studentId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidStudentCommandServiceImpl(DefaultCommands<StudentRecord, Integer> defaultStudentCommands,
                                          DefaultQueries<StudentRecord, Integer> defaultStudentQueries,
                                          ForeignQueries<Integer> foreignStudentQueries,
                                          DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries) {
        this.defaultStudentCommands = defaultStudentCommands;
        this.defaultStudentQueries = defaultStudentQueries;
        this.foreignStudentQueries = foreignStudentQueries;
        this.defaultCustomerQueries = defaultCustomerQueries;
    }

    // PRIVATE

    private final DefaultCommands<StudentRecord, Integer> defaultStudentCommands;
    private final DefaultQueries<StudentRecord, Integer> defaultStudentQueries;
    private final ForeignQueries<Integer> foreignStudentQueries;

    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;

}
