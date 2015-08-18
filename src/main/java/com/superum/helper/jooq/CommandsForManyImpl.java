package com.superum.helper.jooq;

import com.superum.helper.field.ManyDefined;
import org.jooq.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CommandsForManyImpl<R extends Record, Primary, Secondary> extends DefaultSqlImpl<R, Primary>
        implements CommandsForMany<Primary, Secondary> {

    @Override
    public <T extends ManyDefined<Primary, Secondary>> int create(T bodyOfMany, int partitionId) {
        return bodyOfMany.secondaryValues()
                .foldLeft(sql.insertInto(table, partitionField, primaryField(), secondaryField),
                        (step, secondary) -> step.values(partitionId, bodyOfMany.primaryValue(), secondary))
                .execute();
    }

    @Override
    public <T extends ManyDefined<Primary, Secondary>> int delete(T bodyOfMany, int partitionId) {
        Condition condition = bodyOfMany.secondaryValues()
                .foldLeft(primaryAndPartition(bodyOfMany.primaryValue(), partitionId),
                        (step, secondary) -> step.and(secondary(secondary)));

        return sql.deleteFrom(table)
                .where(condition)
                .execute();
    }

    @Override
    public int deletePrimary(Primary value, int partitionId) {
        return sql.deleteFrom(table)
                .where(primaryAndPartition(value, partitionId))
                .execute();
    }

    @Override
    public int deleteSecondary(Secondary value, int partitionId) {
        return sql.deleteFrom(table)
                .where(secondaryAndPartition(value, partitionId))
                .execute();
    }

    // CONSTRUCTORS

    public CommandsForManyImpl(DSLContext sql, Table<R> table, TableField<R, Primary> primaryField,
                               TableField<R, Secondary> secondaryField, TableField<R, Integer> partitionField) {
        super(sql, table, primaryField, partitionField);

        this.secondaryField = secondaryField;
    }

    // PRIVATE

    private final TableField<R, Secondary> secondaryField;

    private TableField<R, Primary> primaryField() {
        return keyField;
    }

    private Condition primaryAndPartition(Primary value, int partitionId) {
        return idAndPartition(value, partitionId);
    }

    private Condition secondary(Secondary value) {
        return secondaryField.eq(value);
    }

    private Condition secondaryAndPartition(Secondary value, int partitionId) {
        return secondary(value).and(partitionId(partitionId));
    }

}
