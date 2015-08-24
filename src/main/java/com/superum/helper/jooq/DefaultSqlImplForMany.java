package com.superum.helper.jooq;

import org.jooq.*;

/**
 * Contains SQL helper methods for a many-to-many table; also, sets up the constructor
 * @param <R> JOOQ generated table record for a table
 * @param <Primary> one of the fields, which is usually less prevalent when creating; i.e. when creating
 *                 group-to-student relationship, group would be primary, because it is more likely that a lot of
 *                 students join a group, rather than a student joins a lot of groups
 * @param <Secondary> one of the fields, which is usually less prevalent when creating; using the above example,
 *                   student would be secondary
 */
public abstract class DefaultSqlImplForMany<R extends Record, Primary, Secondary> extends DefaultSqlImpl<R, Primary>
        implements DefaultSqlForMany<R, Primary, Secondary> {

    @Override
    public Condition primaryAndPartition(Primary value, int partitionId) {
        return idAndPartition(value, partitionId);
    }

    @Override
    public Condition secondaryAndPartition(Secondary value, int partitionId) {
        return secondary(value).and(partitionId(partitionId));
    }

    // CONSTRUCTORS

    protected DefaultSqlImplForMany(DSLContext sql, Table<R> table, TableField<R, Primary> primaryField,
                                    TableField<R, Secondary> secondaryField, TableField<R, Integer> partitionField) {
        super(sql, table, primaryField, partitionField);

        this.secondaryField = secondaryField;
    }

    // PROTECTED

    protected final TableField<R, Secondary> secondaryField;

    protected TableField<R, Primary> primaryField() {
        return keyField;
    }

    protected Condition secondary(Secondary value) {
        return secondaryField.eq(value);
    }

}
