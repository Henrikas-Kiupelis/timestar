package com.superum.config;

import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.db.generated.timestar.tables.records.TeacherRecord;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.DefaultQueriesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.db.generated.timestar.Tables.TEACHER;

@Configuration
@Lazy
public class DefaultSqlConfig {

    @Autowired
    PersistenceContext persistenceContext;

    @Bean
    public DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries() {
        return new DefaultQueriesImpl<>(persistenceContext.dsl(), TEACHER, TEACHER.ID, TEACHER.PARTITION_ID);
    }

    @Bean
    public DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries() {
        return new DefaultQueriesImpl<>(persistenceContext.dsl(), CUSTOMER, CUSTOMER.ID, CUSTOMER.PARTITION_ID);
    }

}
