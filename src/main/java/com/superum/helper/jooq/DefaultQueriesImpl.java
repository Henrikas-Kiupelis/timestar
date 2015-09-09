package com.superum.helper.jooq;

import eu.goodlike.neat.Null;
import org.jooq.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Contains methods for commands on tables
 * @param <R> JOOQ generated table record for a table
 * @param <ID> primary key field type for this table
 */
public class DefaultQueriesImpl<R extends Record, ID> extends DefaultSqlImpl<R, ID> implements DefaultQueries<R, ID> {

    @Override
    public <T> Optional<T> read(ID id, int partitionId, Function<R, T> mapper) {
        Null.check(id, mapper).ifAny("Primary key or mapper cannot be null");

        return sql.selectFrom(table)
                .where(idAndPartition(id, partitionId))
                .fetch().stream().findAny()
                .map(mapper);
    }

    @Override
    public boolean existsForCondition(Condition condition) {
        Null.check(condition).ifAny("Condition cannot be null");

        return sql.fetchExists(table, condition);
    }

    @Override
    public <T> List<T> readForCondition(int page, int amount, Condition condition, RecordMapper<R, T> mapper) {
        Null.check(condition, mapper).ifAny("Condition or mapper cannot be null");

        return sql.selectFrom(table)
                .where(condition)
                .orderBy(keyField)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(mapper);
    }

    @Override
    public int countForCondition(Condition condition) {
        Null.check(condition).ifAny("Condition cannot be null");

        return sql.fetchCount(table, condition);
    }

    @Override
    public <T> Optional<T> readFieldForCondition(TableField<R, T> field, Condition condition) {
        Null.check(field, condition).ifAny("Field or condition cannot be null");

        return sql.select(field)
                .from(table)
                .where(condition)
                .limit(1)
                .fetch().stream().findAny()
                .map(record -> record.getValue(field));
    }

    // CONSTRUCTORS

    public DefaultQueriesImpl(DSLContext sql, Table<R> table, TableField<R, ID> keyField, TableField<R, Integer> partitionField) {
        super(sql, table, keyField, partitionField);
    }

}
