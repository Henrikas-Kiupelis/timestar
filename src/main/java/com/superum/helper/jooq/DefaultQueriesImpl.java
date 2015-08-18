package com.superum.helper.jooq;

import org.jooq.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DefaultQueriesImpl<R extends Record, ID> extends DefaultSqlImpl<R, ID> implements DefaultQueries<R, ID> {

    @Override
    public <T> Optional<T> read(ID id, int partitionId, Function<R, T> mapper) {
        return sql.selectFrom(table)
                .where(idAndPartition(id, partitionId))
                .fetch().stream().findAny()
                .map(mapper);
    }

    @Override
    public boolean exists(ID id, int partitionId) {
        if (id == null)
            throw new IllegalArgumentException("Primary key value cannot be null");

        return sql.fetchExists(sql.selectOne().from(table).where(idAndPartition(id, partitionId)));
    }

    @Override
    public <T> List<T> readForCondition(int page, int amount, Condition condition, RecordMapper<R, T> mapper) {
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
        return sql.fetchCount(table, condition);
    }

    // CONSTRUCTORS

    public DefaultQueriesImpl(DSLContext sql, Table<R> table, TableField<R, ID> keyField, TableField<R, Integer> partitionField) {
        super(sql, table, keyField, partitionField);
    }

}
