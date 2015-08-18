package com.superum.helper.jooq;

import org.jooq.Condition;
import org.jooq.Record;

/**
 * Contains SQL helper methods
 * @param <R> JOOQ generated table record for a table
 * @param <ID> primary key field type for this table
 */
public interface DefaultSql<R extends Record, ID> {

    /**
     * @return condition which checks if the record's partition field is equal to partitionId
     */
    Condition partitionId(int partitionId);

    /**
     * @return condition which checks if the record's partition field is equal to partitionId, and primary key is equal
     * to id
     */
    Condition idAndPartition(ID id, int partitionId);

}
