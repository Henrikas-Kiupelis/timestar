package com.superum.api.v2.customer;

import com.superum.api.v2.teacher.TeacherNotFoundException;
import com.superum.helper.jooq.DefaultQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.CustomerRecord;
import timestar_v2.tables.records.TeacherRecord;

import java.util.List;

@Service
public class ValidCustomerQueryServiceImpl implements ValidCustomerQueryService {

    @Override
    public ValidCustomerDTO readById(int customerId, int partitionId) {
        return defaultCustomerQueries.read(customerId, partitionId, ValidCustomerDTO::valueOf)
                .orElseThrow(() -> new CustomerNotFoundException("Couldn't find customer with id " + customerId));
    }

    @Override
    public List<ValidCustomerDTO> readForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return customersForTeacher.fetch(teacherId, page, amount, partitionId);
    }

    @Override
    public List<ValidCustomerDTO> readAll(int page, int amount, int partitionId) {
        return defaultCustomerQueries.readAll(page, amount, partitionId, ValidCustomerDTO::valueOf);
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return customersForTeacher.count(teacherId, partitionId);
    }

    @Override
    public int countAll(int partitionId) {
        return defaultCustomerQueries.countAll(partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidCustomerQueryServiceImpl(CustomersForTeacherFetcher customersForTeacher,
                                         DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries,
                                         DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries) {
        this.customersForTeacher = customersForTeacher;
        this.defaultCustomerQueries = defaultCustomerQueries;
        this.defaultTeacherQueries = defaultTeacherQueries;
    }

    // PRIVATE

    private final CustomersForTeacherFetcher customersForTeacher;
    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;

}
