package com.superum.helper.jooq;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DefaultQueries<R extends Record, ID> extends DefaultSql<R, ID> {

    <T> Optional<T> read(ID id, int partitionId, Function<R, T> mapper);

    /**
     * @return true if a record with primary key value id exists in given partition; false otherwise
     * @throws NullPointerException if primary key is null
     */
    boolean exists(ID id, int partitionId);

    <T> List<T> readForCondition(int page, int amount, Condition condition, RecordMapper<R, T> mapper);

    default <T> List<T> readAll(int page, int amount, int partitionId, RecordMapper<R, T> mapper) {
        return readForCondition(page, amount, partitionId(partitionId), mapper);
    }

    default <T, F_ID> List<T>  readForForeignKey(int page, int amount, int partitionId,
                                                 Field<F_ID> foreignKey, F_ID keyValue, RecordMapper<R, T> mapper) {
        return readForCondition(page, amount, foreignKey.eq(keyValue).and(partitionId(partitionId)), mapper);
    }

    int countForCondition(Condition condition);

    default int countAll(int partitionId) {
        return countForCondition(partitionId(partitionId));
    }

    default <T, F_ID> int countForForeignKey(int partitionId, Field<F_ID> foreignKey, F_ID keyValue) {
        return countForCondition(foreignKey.eq(keyValue).and(partitionId(partitionId)));
    }

}
