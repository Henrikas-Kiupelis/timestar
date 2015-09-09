package com.superum.helper.jooq;

import eu.goodlike.neat.Null;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * Contains methods for commands on many-to-many tables
 * @param <Primary> one of the fields, which is usually less prevalent when creating; i.e. when creating
 *                 group-to-student relationship, group would be primary, because it is more likely that a lot of
 *                 students join a group, rather than a student joins a lot of groups
 * @param <Secondary> one of the fields, which is usually less prevalent when creating; using the above example,
 *                   student would be secondary
 */
public final class QueriesForManyImpl<R extends Record, Primary, Secondary>
        extends DefaultSqlImplForMany<R, Primary, Secondary>
        implements QueriesForMany<Primary, Secondary> {

    @Override
    public boolean existsPrimary(Primary value, int partitionId) {
        Null.check(value).ifAny("Primary value cannot be null");
        return sql.fetchExists(table, primaryAndPartition(value, partitionId));
    }

    @Override
    public boolean existsSecondary(Secondary value, int partitionId) {
        Null.check(value).ifAny("Primary value cannot be null");
        return sql.fetchExists(table, secondaryAndPartition(value, partitionId));
    }

    // CONSTRUCTORS

    public QueriesForManyImpl(DSLContext sql, Table<R> table, TableField<R, Primary> primaryField,
                              TableField<R, Secondary> secondaryField, TableField<R, Integer> partitionField) {
        super(sql, table, primaryField, secondaryField, partitionField);
    }


}
