package com.superum.config;

import com.superum.helper.PartitionAccount;
import eu.goodlike.libraries.jooq.Queries;
import eu.goodlike.libraries.jooq.QueriesForeign;
import eu.goodlike.libraries.jooq.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import timestar_v2.tables.records.*;

import static timestar_v2.Tables.*;

@Configuration
@Lazy
public class JOOQSQLConfig {

    @Autowired
    PersistenceContext persistenceContext;

    @Bean
    public Queries<LessonRecord, Long> lessonQueries() {
        Queries<LessonRecord, Long> queries = SQL.queriesFor(persistenceContext.dsl(), LESSON, LESSON.ID);
        queries.setUniversalCondition(() -> LESSON.PARTITION_ID.eq(new PartitionAccount().partitionId()));
        return queries;
    }

    @Bean
    public Queries<GroupOfStudentsRecord, Integer> groupQueries() {
        Queries<GroupOfStudentsRecord, Integer> queries = SQL.queriesFor(persistenceContext.dsl(), GROUP_OF_STUDENTS, GROUP_OF_STUDENTS.ID);
        queries.setUniversalCondition(() -> GROUP_OF_STUDENTS.PARTITION_ID.eq(new PartitionAccount().partitionId()));
        return queries;
    }

    @Bean
    public Queries<TeacherRecord, Integer> teacherQueries() {
        Queries<TeacherRecord, Integer> queries = SQL.queriesFor(persistenceContext.dsl(), TEACHER, TEACHER.ID);
        queries.setUniversalCondition(() -> TEACHER.PARTITION_ID.eq(new PartitionAccount().partitionId()));
        return queries;
    }

    @Bean
    public Queries<CustomerRecord, Integer> customerQueries() {
        Queries<CustomerRecord, Integer> queries = SQL.queriesFor(persistenceContext.dsl(), CUSTOMER, CUSTOMER.ID);
        queries.setUniversalCondition(() -> CUSTOMER.PARTITION_ID.eq(new PartitionAccount().partitionId()));
        return queries;
    }

    @Bean
    public Queries<StudentRecord, Integer> studentQueries() {
        Queries<StudentRecord, Integer> queries = SQL.queriesFor(persistenceContext.dsl(), STUDENT, STUDENT.ID);
        queries.setUniversalCondition(() -> STUDENT.PARTITION_ID.eq(new PartitionAccount().partitionId()));
        return queries;
    }

    @Bean
    public QueriesForeign<Long> lessonForeignQueries() {
        QueriesForeign<Long> queries = SQL.queriesFor(persistenceContext.dsl(), LESSON_ATTENDANCE.LESSON_ID);
        queries.setUniversalCondition(() -> LESSON_ATTENDANCE.PARTITION_ID.eq(new PartitionAccount().partitionId()));
        return queries;
    }

}
