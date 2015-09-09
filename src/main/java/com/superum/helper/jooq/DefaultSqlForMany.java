package com.superum.helper.jooq;

import org.jooq.Condition;
import org.jooq.Record;

/**
 * Contains SQL helper methods for a many-to-many table
 * @param <R> JOOQ generated table record for a table
 * @param <Primary> one of the fields, which is usually less prevalent when creating; i.e. when creating
 *                 group-to-student relationship, group would be primary, because it is more likely that a lot of
 *                 students join a group, rather than a student joins a lot of groups
 * @param <Secondary> one of the fields, which is usually less prevalent when creating; using the above example,
 *                   student would be secondary
 */
public interface DefaultSqlForMany<R extends Record, Primary, Secondary> {

    /**
     * @return condition which checks if the record's partition field is equal to partitionId
     */
    Condition partitionId(int partitionId);

    /**
     * @return condition which checks if the record's partition field is equal to partitionId, and primary value is
     * equal to given one
     * @throws NullPointerException if value is null
     */
    Condition primaryAndPartition(Primary value, int partitionId);

    /**
     * @return condition which checks if the record's partition field is equal to partitionId, and secondary value
     * is equal to given one
     * @throws NullPointerException if value is null
     */
    Condition secondaryAndPartition(Secondary value, int partitionId);

}
