package com.superum.helper.jooq;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableField;

/**
 * Can quickly check if a particular table has a record with particular primary key in a particular partition
 *
 * @param <TableRecord> jooq Record type for the table that will be checked
 * @param <ID> primary key type for this table
 */
public final class QuickIdExistenceChecker<TableRecord extends Record, ID> {

    /**
     * @return true if a record with primary key value id exists in given partition; false otherwise
     *
     * @throws IllegalArgumentException if any arguments are null
     */
    public boolean check(ID id, int partitionId) {
        if (id == null)
            throw new IllegalArgumentException("Primary key value cannot be null");

        return sql.fetchExists(sql.selectOne().from(idField.getTable()).where(idAndPartition(id, partitionId)));
    }

    // CONSTRUCTORS

    public QuickIdExistenceChecker(DSLContext sql, TableField<TableRecord, ID> idField, TableField<TableRecord, Integer> partitionField) {
        if (sql == null)
            throw new IllegalArgumentException("DSLContext cannot be null");

        if (idField == null)
            throw new IllegalArgumentException("Primary key field cannot be null");

        if (partitionField == null)
            throw new IllegalArgumentException("Partition id field cannot be null");

        this.sql = sql;
        this.idField = idField;
        this.partitionField = partitionField;
    }

    // PRIVATE

    private final DSLContext sql;
    private final TableField<TableRecord, ID> idField;
    private final TableField<TableRecord, Integer> partitionField;

    /**
     * @return Condition which checks for primary key value and partition id
     */
    private Condition idAndPartition(ID id, int partitionId) {
        return idField.eq(id).and(partitionField.eq(partitionId));
    }

}
