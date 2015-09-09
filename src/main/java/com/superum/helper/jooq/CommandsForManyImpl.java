package com.superum.helper.jooq;

import com.superum.helper.field.ManyDefined;
import eu.goodlike.neat.Null;
import org.jooq.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains methods for commands on many-to-many tables
 * @param <R> JOOQ generated table record for a table
 * @param <Primary> one of the fields, which is usually less prevalent when creating; i.e. when creating
 *                 group-to-student relationship, group would be primary, because it is more likely that a lot of
 *                 students join a group, rather than a student joins a lot of groups
 * @param <Secondary> one of the fields, which is usually less prevalent when creating; using the above example,
 *                   student would be secondary
 */
@Transactional
public class CommandsForManyImpl<R extends Record, Primary, Secondary>
        extends DefaultSqlImplForMany<R, Primary, Secondary>
        implements CommandsForMany<Primary, Secondary> {

    @Override
    public <T extends ManyDefined<Primary, Secondary>> int create(T bodyOfMany, int partitionId) {
        Null.check(bodyOfMany).ifAny("Body cannot be null");
        return bodyOfMany.secondaryValues()
                .foldLeft(sql.insertInto(table, partitionField, primaryField(), secondaryField),
                        (step, secondary) -> step.values(partitionId, bodyOfMany.primaryValue(), secondary))
                .execute();
    }

    @Override
    public <T extends ManyDefined<Primary, Secondary>> int delete(T bodyOfMany, int partitionId) {
        Null.check(bodyOfMany).ifAny("Body cannot be null");
        Condition condition = bodyOfMany.secondaryValues()
                .foldLeft(primaryAndPartition(bodyOfMany.primaryValue(), partitionId),
                        (step, secondary) -> step.and(secondary(secondary)));

        return sql.deleteFrom(table)
                .where(condition)
                .execute();
    }

    @Override
    public int deletePrimary(Primary value, int partitionId) {
        Null.check(value).ifAny("Primary value cannot be null");
        return sql.deleteFrom(table)
                .where(primaryAndPartition(value, partitionId))
                .execute();
    }

    @Override
    public int deleteSecondary(Secondary value, int partitionId) {
        Null.check(value).ifAny("Secondary value cannot be null");
        return sql.deleteFrom(table)
                .where(secondaryAndPartition(value, partitionId))
                .execute();
    }

    // CONSTRUCTORS

    public CommandsForManyImpl(DSLContext sql, Table<R> table, TableField<R, Primary> primaryField,
                              TableField<R, Secondary> secondaryField, TableField<R, Integer> partitionField) {
        super(sql, table, primaryField, secondaryField, partitionField);
    }

}
