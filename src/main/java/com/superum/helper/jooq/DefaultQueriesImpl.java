package com.superum.helper.jooq;

import com.superum.helper.NullChecker;
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
        NullChecker.check(id, mapper).notNull("Primary key or mapper cannot be null");

        return sql.selectFrom(table)
                .where(idAndPartition(id, partitionId))
                .fetch().stream().findAny()
                .map(mapper);
    }

    @Override
    public boolean existsForCondition(Condition condition) {
        if (condition == null)
            throw new IllegalArgumentException("Condition cannot be null");

        return sql.fetchExists(sql.selectOne().from(table).where(condition).limit(1));
    }

    @Override
    public <T> List<T> readForCondition(int page, int amount, Condition condition, RecordMapper<R, T> mapper) {
        NullChecker.check(condition, mapper).notNull("Condition or mapper cannot be null");

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
        if (condition == null)
            throw new IllegalArgumentException("Condition cannot be null");

        return sql.fetchCount(table, condition);
    }

    // CONSTRUCTORS

    public DefaultQueriesImpl(DSLContext sql, Table<R> table, TableField<R, ID> keyField, TableField<R, Integer> partitionField) {
        super(sql, table, keyField, partitionField);
    }

}
