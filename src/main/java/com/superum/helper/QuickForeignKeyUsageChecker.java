package com.superum.helper;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.HashMap;
import java.util.Map;
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
                        .where(foreignKeysAndPartitions.entrySet().stream()
                                .map(entry -> entry.getKey().eq(id).and(entry.getValue().eq(partitionId)))
                                .reduce(Condition::or)
                                .orElseThrow(() -> new AssertionError("At least one field should have been provided to the checker!"))));
    }

    // CONSTRUCTORS

    public static <ID> DSLContextStep<ID> newRequiredBuilder(Class<ID> clazz) {
        return new Builder<>();
    }

    private QuickForeignKeyUsageChecker(DSLContext sql, Map<TableField<?, ID>, TableField<?, Integer>> foreignKeysAndPartitions) {
        if (sql == null)
            throw new IllegalArgumentException("DSLContext cannot be null");

        if (foreignKeysAndPartitions == null || foreignKeysAndPartitions.values().size() < 1)
            throw new IllegalArgumentException("You must provide at least a single field to the checker!");

        this.tables = foreignKeysAndPartitions.keySet().stream()
                .map(TableField::getTable)
                .collect(Collectors.toSet());

        if (!foreignKeysAndPartitions.values().stream()
                .map(TableField::getTable)
                .collect(Collectors.toSet())
                .equals(tables))
            throw new IllegalArgumentException("For every table containing a foreign key, a partition id field must be provided");

        this.sql = sql;
        this.foreignKeysAndPartitions = foreignKeysAndPartitions;
    }

    // PRIVATE

    private final DSLContext sql;
    private final Set<Table<?>> tables;
    private final Map<TableField<?, ID>, TableField<?, Integer>> foreignKeysAndPartitions;

    // BUILDER

    public interface DSLContextStep<ID> {
        BuildStep<ID> withDSLContext(DSLContext sql);
    }

    public interface BuildStep<ID> {
        BuildStep<ID> add(TableField<?, ID> foreignKey, TableField<?, Integer> partitionField);
        QuickForeignKeyUsageChecker<ID> build();
    }

    public static final class Builder<ID> implements DSLContextStep<ID>, BuildStep<ID> {

        @Override
        public BuildStep<ID> withDSLContext(DSLContext sql) {
            this.sql = sql;
            return this;
        }

        @Override
        public BuildStep<ID> add(TableField<?, ID> foreignKey, TableField<?, Integer> partitionField) {
            foreignKeysAndPartitions.put(foreignKey, partitionField);
            return this;
        }

        @Override
        public QuickForeignKeyUsageChecker<ID> build() {
            return new QuickForeignKeyUsageChecker<>(sql, foreignKeysAndPartitions);
        }

        // PRIVATE

        private DSLContext sql;
        private final Map<TableField<?, ID>, TableField<?, Integer>> foreignKeysAndPartitions = new HashMap<>();

    }

}
