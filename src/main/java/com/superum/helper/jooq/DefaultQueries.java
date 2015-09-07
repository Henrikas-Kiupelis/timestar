package com.superum.helper.jooq;

import eu.goodlike.neat.Null;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.TableField;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Contains methods for queries on tables
 * @param <R> JOOQ generated table record for a table
 * @param <ID> primary key field type for this table
 */
public interface DefaultQueries<R extends Record, ID> extends DefaultSql<R, ID> {

    /**
     * @param id primary key value to look for
     * @param partitionId partition in which this value should exist
     * @param mapper function to turn resulting database record into an object
     * @param <T> type of object which maps from the database record
     * @return object whose primary key value equals to id; Optional.empty() if database couldn't find it in given
     * partition
     * @throws NullPointerException if primary key or mapper is null
     */
    <T> Optional<T> read(ID id, int partitionId, Function<R, T> mapper);

    /**
     * @return true if a record for a certain condition exists; false otherwise
     * @throws IllegalArgumentException if condition is null
     */
    boolean existsForCondition(Condition condition);

    /**
     * @return true if a record with primary key value id exists in given partition; false otherwise
     * @throws NullPointerException if primary key is null
     */
    default boolean exists(ID id, int partitionId) {
        Null.check(id).ifAny("Primary key value cannot be null");
        return existsForCondition(idAndPartition(id, partitionId));
    }

    /**
     * @return true if a record with a certain key exists in given partition; false otherwise
     * @throws NullPointerException if key or keyValue is null
     */
    default <F_ID> boolean existsForKey(int partitionId, TableField<R, F_ID> key, F_ID keyValue) {
        Null.check(key, keyValue).ifAny("Key field and its value cannot be null");
        return existsForCondition(key.eq(keyValue).and(partitionId(partitionId)));
    }

    /**
     * @return list of objects for a certain condition; paged, with offset;
     * @throws NullPointerException if condition or mapper is null
     */
    <T> List<T> readForCondition(int page, int amount, Condition condition, RecordMapper<R, T> mapper);

    /**
     * @return list of all objects; paged, with offset;
     * @throws NullPointerException if mapper is null
     */
    default <T> List<T> readAll(int page, int amount, int partitionId, RecordMapper<R, T> mapper) {
        return readForCondition(page, amount, partitionId(partitionId), mapper);
    }

    /**
     * @return list of objects with a certain foreign key; paged, with offset;
     * @throws NullPointerException if foreignKey, keyValue or mapper is null
     */
    default <T, F_ID> List<T> readForForeignKey(int page, int amount, int partitionId,
                                                 TableField<R, F_ID> foreignKey, F_ID keyValue, RecordMapper<R, T> mapper) {
        Null.check(foreignKey, keyValue).ifAny("Foreign field and its value cannot be null");
        return readForCondition(page, amount, foreignKey.eq(keyValue).and(partitionId(partitionId)), mapper);
    }

    /**
     * @return count of objects with a certain condition
     * @throws NullPointerException if condition is null
     */
    int countForCondition(Condition condition);

    /**
     * @return count of all objects
     */
    default int countAll(int partitionId) {
        return countForCondition(partitionId(partitionId));
    }

    /**
     * @return count of objects with a certain foreign key
     * @throws NullPointerException if foreignKey or keyValue is null
     */
    default <T, F_ID> int countForForeignKey(int partitionId, TableField<R, F_ID> foreignKey, F_ID keyValue) {
        Null.check(foreignKey, keyValue).ifAny("Foreign field and its value cannot be null");
        return countForCondition(foreignKey.eq(keyValue).and(partitionId(partitionId)));
    }

    /**
     * @return a single field value of a record for given condition; enforce uniqueness with condition or suffer!
     * @throws NullPointerException if field or condition is null
     */
    <T> Optional<T> readFieldForCondition(TableField<R, T> field, Condition condition);

    /**
     * @return a single field value of a record with certain id
     * @throws NullPointerException if id is null
     */
    default <T> Optional<T> readField(TableField<R, T> field, ID id, int partitionId) {
        Null.check(id).ifAny("Primary key value cannot be null");
        return readFieldForCondition(field, idAndPartition(id, partitionId));
    }

}
