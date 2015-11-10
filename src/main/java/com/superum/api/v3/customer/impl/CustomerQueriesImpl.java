package com.superum.api.v3.customer.impl;

import com.superum.api.v3.customer.CustomerErrors;
import com.superum.api.v3.customer.CustomerQueries;
import com.superum.api.v3.customer.CustomerSerializer;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import com.superum.api.v3.customer.sql.CustomersForTeacher;
import com.superum.api.v3.teacher.TeacherErrors;
import eu.goodlike.libraries.jooq.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.CustomerRecord;
import timestar_v2.tables.records.TeacherRecord;

import java.util.List;

@Service
public class CustomerQueriesImpl implements CustomerQueries {

    @Override
    public FetchedCustomer readById(int id) {
        return customerQueries.read(id, customerSerializer::toReturnable)
                .orElseThrow(() -> CustomerErrors.customerIdError(id));
    }

    @Override
    public List<FetchedCustomer> readForTeacher(int teacherId, int page, int amount) {
        if (!teacherQueries.exists(teacherId))
            throw TeacherErrors.teacherIdError(teacherId);

        return customersForTeacher.fetch(teacherId, page, amount);
    }

    @Override
    public List<FetchedCustomer> readAll(int page, int amount) {
        return customerQueries.readAll(page, amount, customerSerializer::toReturnable);
    }

    @Override
    public int countForTeacher(int teacherId) {
        if (!teacherQueries.exists(teacherId))
            throw TeacherErrors.teacherIdError(teacherId);

        return customersForTeacher.count(teacherId);
    }

    @Override
    public int countAll() {
        return customerQueries.countAll();
    }


    // CONSTRUCTORS

    @Autowired
    public CustomerQueriesImpl(Queries<CustomerRecord, Integer> customerQueries, CustomerSerializer customerSerializer,
                               Queries<TeacherRecord, Integer> teacherQueries, CustomersForTeacher customersForTeacher) {
        this.customerQueries = customerQueries;
        this.customerSerializer = customerSerializer;
        this.teacherQueries = teacherQueries;
        this.customersForTeacher = customersForTeacher;
    }

    // PRIVATE

    private final Queries<CustomerRecord, Integer> customerQueries;
    private final CustomerSerializer customerSerializer;
    private final Queries<TeacherRecord, Integer> teacherQueries;
    private final CustomersForTeacher customersForTeacher;

}
