package com.superum.helper;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 * Can quickly check if any given tables have records with particular foreign key in a particular partition
 * </pre>
 * @param <ID> foreign key type in the tables
 */
public class QuickForeignKeyUsageChecker<ID> {

    /**
     * @return true if a record with foreign key value id exists in given partition; false otherwise
     *
     * @throws IllegalArgumentException if any arguments are null
     */
    public boolean check(ID id, int partitionId) {
        if (id == null)
            throw new IllegalArgumentException("Foreign key value cannot be null");

        return sql.fetchExists(
                sql.selectOne()
                        .from(tables)
                        .where(foreignKeys.stream()
                                .map(field -> field.eq(id))
                                .reduce(Condition::and)
                                .orElseThrow(() -> new AssertionError("At least one field should have been provided to the checker!")))
                        .and(partitionFields.stream()
                                .map(field -> field.eq(partitionId))
                                .reduce(Condition::and)
                                .orElseThrow(() -> new AssertionError("At least one field should have been provided to the checker!"))));
    }

    // CONSTRUCTORS

    private QuickForeignKeyUsageChecker(DSLContext sql, Set<TableField<?, ID>> foreignKeys, Set<TableField<?, Integer>> partitionFields) {
        if (sql == null)
            throw new IllegalArgumentException("DSLContext cannot be null");

        if (foreignKeys == null || partitionFields == null || foreignKeys.size() < 1)
            throw new IllegalArgumentException("You must provide at least a single field to the checker!");

        this.tables = foreignKeys.stream()
                .map(TableField::getTable)
                .collect(Collectors.toSet());

        if (!partitionFields.stream()
                .map(TableField::getTable)
                .collect(Collectors.toSet())
                .equals(tables))
            throw new IllegalArgumentException("For every table containing a foreign key, a partition id field must be provided");

        this.sql = sql;
        this.foreignKeys = foreignKeys;
        this.partitionFields = partitionFields;
    }

    // PRIVATE

    private final DSLContext sql;
    private final Set<Table<?>> tables;
    private final Set<TableField<?, ID>> foreignKeys;
    private final Set<TableField<?, Integer>> partitionFields;

}
