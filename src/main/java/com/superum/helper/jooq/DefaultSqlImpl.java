package com.superum.helper.jooq;

import org.jooq.*;

public abstract class DefaultSqlImpl<R extends Record, ID> implements DefaultSql<R, ID> {

    public Condition partitionId(int partitionId) {
        return partitionField.eq(partitionId);
    }

    public Condition idAndPartition(ID id, int partitionId) {
        return keyField.eq(id).and(partitionId(partitionId));
    }

    // CONSTRUCTORS

    protected DefaultSqlImpl(DSLContext sql, Table<R> table, TableField<R, ID> keyField, TableField<R, Integer> partitionField) {
        this.sql = sql;
        this.table = table;
        this.keyField = keyField;
        this.partitionField = partitionField;
    }

    // PROTECTED

    protected final DSLContext sql;
    protected final Table<R> table;
    protected final TableField<R, ID> keyField;
    protected final TableField<R, Integer> partitionField;

}
