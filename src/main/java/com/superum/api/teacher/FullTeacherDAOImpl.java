package com.superum.api.teacher;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FullTeacherDAOImpl implements FullTeacherDAO {

    @Override
    public int exists(FullTeacher customer, int partitionId) {
        return 1;
    }

    // CONSTRUCTORS

    @Autowired
    public FullTeacherDAOImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
