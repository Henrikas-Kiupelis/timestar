package com.superum.helper;

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
public class QuickIdExistenceChecker<TableRecord extends Record, ID> {

    /**
     * @return true if a record with primary key value id exists in given partition; false otherwise
     */
    public boolean check(ID id, int partitionId) {
        return sql.fetchExists(sql.selectOne().from(idField.getTable()).where(idAndPartition(id, partitionId)));
    }

    // CONSTRUCTORS

    public QuickIdExistenceChecker(DSLContext sql, TableField<TableRecord, ID> idField, TableField<TableRecord, Integer> partitionField) {
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
