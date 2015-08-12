package com.superum.helper.jooq;

import com.superum.helper.NullChecker;
import com.superum.helper.field.Defined;
import com.superum.helper.field.core.MappedField;
import org.jooq.*;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * <pre>
 * Creates default queries (or at least parts of them) for simple use case scenarios
 *
 * The following queries should be creatable:
 * on a table:
 * create()     creates a single record
 * read()       reads a single record
 * update()     updates a single record
 * delete()     deletes a single record
 * exists()     checks if a record exists
 * count()      counts the amount of records
 * readAll()    reads all records, paged, with offset
 * on other tables:
 * isUsed()     checks if a foreign key constraint is used
 * </pre>
 */
public class DefaultQueryMaker {

    /**
     * <pre>
     * Creates a QueryMaker for queries on a table
     *
     * Some of the queries that are available:
     * create()     creates a single record
     * read()       reads a single record
     * update()     updates a single record
     * delete()     deletes a single record
     * exists()     checks if a record exists
     * count()      counts the amount of records
     * readAll()    reads all records, paged, with offset
     * </pre>
     * @param <R> JOOQ generated table record for a table
     * @param <ID> primary key field type for this table
     */
    public static <R extends Record, ID>  Queries<R, ID> forTable(DSLContext sql, Table<R> table,
                                                                  TableField<R, ID> keyField, TableField<R, Integer> partitionField) {
        NullChecker.check(table, keyField, partitionField).notNull("Table, id field and partition id field cannot be null");

        return new QueryMaker<>(sql, table, keyField, partitionField);
    }

    /**
     * <pre>
     * Creates a QueryMaker for queries on other tables
     *
     * Some of the queries that are available:
     * isUsed()     checks if a foreign key constraint is used
     * </pre>
     * @param <ID> foreign key type
     */
    @SafeVarargs
    public static <ID> ForeignQueries<ID> forOtherTables(DSLContext sql, TableField<?, ID>... foreignKeys) {
        NullChecker.check((Object)foreignKeys).notNull("Foreign keys cannot be null");
        NullChecker.check((Object[])foreignKeys).notNull("None of the foreign keys can be null");

        return new ForeignQueryMaker<>(sql, foreignKeys);
    }

    // INNER

    /**
     * Contains methods to create queries on a table
     * @param <R> JOOQ generated table record for a table
     * @param <ID> primary key field type for this table
     */
    public interface Queries<R extends Record, ID> {

        /**
         * <pre>
         * Creates a single record in the table
         *
         * Uses the definitions in parameter 'body' to fill the fields
         * </pre>
         * @param mapper function, which maps the returned record back to an object of the originating class
         * @param <T> type of the object that contains mapping definitions to this table
         * @return object, created by using mapper on the record returned by the database; Optional.empty if it was
         * null
         */
        <T extends Defined<T, ID>> Optional<T> create(T body, int partitionId, Function<R, T> mapper);

        /**
         * Reads a single record from the table using a particular primary key
         * @param mapper function, which maps the returned record back to an object of the originating class
         * @param <T> type of the object that contains mapping definitions to this table
         * @return object, created by using mapper on the record returned by the database; Optional.empty if it was
         * null
         */
        <T> Optional<T> read(ID id, int partitionId, Function<R, T> mapper);

        /**
         * <pre>
         * Updates a single record in the table
         *
         * Uses the definitions in parameter 'body' to fill the fields
         * </pre>
         * @param <T> type of the object that contains mapping definitions to this table
         * @return amount of records updated; 0 if nothing was updated, 1 if a single record was updated
         */
        <T extends Defined<T, ID>> int update(T body, int partitionId);

        /**
         * Deletes a single record from the table using a particular primary key
         * @return amount of records deleted; 0 if nothing was deleted, 1 if a single record was deleted
         */
        int delete(ID id, int partitionId);

        /**
         * @return true if a record with primary key value id exists in given partition; false otherwise
         * @throws NullPointerException if primary key is null
         */
        boolean exists(ID id, int partitionId);

        /**
         * <pre>
         * Checks if a certain record exists
         *
         * Uses conditionFields() as it is defined for the 'body'; usually, this it returns set fields
         * </pre>
         * @param <T> type of the object that contains mapping definitions to this table
         * @return primary key of the record if it exists, Optional.empty otherwise
         */
        <T extends Defined<T, ID>> Optional<ID> exists(T body, int partitionId);

        /**
         * @return amount of records in a certain partition
         */
        int countAll(int partitionId);

        /**
         * @return list of records in a certain partition; paged, with offset
         * @param mapper function, which maps the returned record back to an object
         */
        <T> List<T> readAll(int partitionId, int page, int amount, RecordMapper<R, T> mapper);

        /**
         * @return amount of records for a custom, join statement
         */
        int countCustom(Function<SelectJoinStep<Record1<Integer>>, SelectConditionStep<Record1<Integer>>> customQuery);

        /**
         * @return list of records for a custom, join statement; paged, with offset
         * @param mapper function, which maps the returned record back to an object
         */
        <T> List<T> readAllCustom(int page, int amount, RecordMapper<Record, T> mapper, Function<SelectJoinStep<Record>, SelectConditionStep<Record>> customQuery);

        /**
         * @return list of records for a custom, join statement; only specified fields are returned; paged, with offset
         * @param mapper function, which maps the returned record back to an object
         */
        <T> List<T> readAllCustom(int page, int amount, RecordMapper<Record, T> mapper, Field<?>[] customFields,
                                  Function<SelectJoinStep<Record>, SelectConditionStep<Record>> customQuery);

        /**
         * @return record for a custom, join statement
         * @param mapper function, which maps the returned record back to an object
         */
        <T> Optional<T> readCustom(ID id, int partitionId, Function<Record, T> mapper,
                                   Function<SelectJoinStep<Record>, SelectWhereStep<Record>> customQuery);

        /**
         * @return record for a custom, join statement; only specified fields are returned
         * @param mapper function, which maps the returned record back to an object
         */
        <T> Optional<T> readCustom(ID id, int partitionId, Function<Record, T> mapper, Field<?>[] customFields,
                                   Function<SelectJoinStep<Record>, SelectWhereStep<Record>> customQuery);
    }

    /**
     * Contains methods to create queries on other tables
     * @param <ID> foreign key type
     */
    public interface ForeignQueries<ID> {
        /**
         * @return true if a given value is used as a foreign key; false otherwise
         */
        boolean isUsed(ID id);
    }

    @Transactional
    private static final class QueryMaker<R extends Record, ID> implements Queries<R, ID> {

        @Override
        public <T extends Defined<T, ID>> Optional<T> create(T body, int partitionId, Function<R, T> mapper) {
            NullChecker.check(body, mapper).notNull("Body and mapper function cannot be null");

            return body.createFields().foldLeft(sql.insertInto(table).set(partitionField, partitionId),
                    (step, field) -> field.insert(step))
                    .returning().fetch().stream().findFirst()
                    .map(mapper);
        }

        @Override
        public <T> Optional<T> read(ID id, int partitionId, Function<R, T> mapper) {
            NullChecker.check(id, mapper).notNull("Id value and mapper function cannot be null");

            return sql.selectFrom(table)
                    .where(idAndPartition(id, partitionId))
                    .fetch().stream().findFirst()
                    .map(mapper);
        }

        @Override
        public <T extends Defined<T, ID>> int update(T body, int partitionId) {
            NullChecker.check(body).notNull("Body cannot be null");

            Tuple2<Optional<MappedField<?>>, Seq<MappedField<?>>> updateFields = body.updateFields().splitAtHead();

            UpdateSetFirstStep<R> updateStepFirst = sql.update(table);
            UpdateSetMoreStep<R> updateStepMore = updateFields.v1.map(field -> field.update(updateStepFirst))
                    .orElseThrow(() -> new IllegalArgumentException("The updated body should have at least one field definition!"));

            return updateFields.v2.foldLeft(updateStepMore, (step, field) -> field.update(step))
                    .where(body.primaryKey().eq().and(partitionId(partitionId)))
                    .execute();
        }

        @Override
        public int delete(ID id, int partitionId) {
            NullChecker.check(id).notNull("Id value cannot be null");

            return sql.deleteFrom(table)
                    .where(idAndPartition(id, partitionId))
                    .execute();
        }

        @Override
        public boolean exists(ID id, int partitionId) {
            NullChecker.check(id).notNull("Id value cannot be null");

            return sql.fetchExists(sql.selectOne().from(table).where(idAndPartition(id, partitionId)));
        }

        @Override
        public <T extends Defined<T, ID>> Optional<ID> exists(T body, int partitionId) {
            return sql.select(keyField)
                    .from(table)
                    .where(body.conditionFields().map(MappedField::eq).reduce(partitionId(partitionId), Condition::and))
                    .fetch().stream().findFirst()
                    .map(record -> record.getValue(keyField));
        }

        @Override
        public int countAll(int partitionId) {
            return sql.fetchCount(table, partitionId(partitionId));
        }

        @Override
        public <T> List<T> readAll(int partitionId, int page, int amount, RecordMapper<R, T> mapper) {
            NullChecker.check(mapper).notNull("Record mapper cannot be null");

            return sql.selectFrom(table)
                    .where(partitionId(partitionId))
                    .groupBy(keyField)
                    .orderBy(keyField)
                    .limit(amount)
                    .offset(page * amount)
                    .fetch()
                    .map(mapper);
        }

        @Override
        public int countCustom(Function<SelectJoinStep<Record1<Integer>>, SelectConditionStep<Record1<Integer>>> customQuery) {
            NullChecker.check(customQuery).notNull("Custom query cannot be null");

            return sql.fetchCount(customQuery.apply(sql.selectOne().from(table)).groupBy(keyField));
        }

        @Override
        public <T> List<T> readAllCustom(int page, int amount, RecordMapper<Record, T> mapper,
                                         Function<SelectJoinStep<Record>, SelectConditionStep<Record>> customQuery) {
            NullChecker.check(mapper, customQuery).notNull("Record mapper and custom query cannot be null");

            return customQuery.apply(sql.select(table.fields()).from(table))
                    .groupBy(keyField)
                    .orderBy(keyField)
                    .limit(amount)
                    .offset(page * amount)
                    .fetch()
                    .map(mapper);
        }

        @Override
        public <T> List<T> readAllCustom(int page, int amount, RecordMapper<Record, T> mapper, Field<?>[] customFields,
                                         Function<SelectJoinStep<Record>, SelectConditionStep<Record>> customQuery) {
            NullChecker.check(mapper, customQuery).notNull("Record mapper and custom query cannot be null");

            return customQuery.apply(sql.select(customFields).from(table))
                    .groupBy(keyField)
                    .orderBy(keyField)
                    .limit(amount)
                    .offset(page * amount)
                    .fetch()
                    .map(mapper);
        }

        @Override
        public <T> Optional<T> readCustom(ID id, int partitionId, Function<Record, T> mapper,
                                Function<SelectJoinStep<Record>, SelectWhereStep<Record>> customQuery) {
            return customQuery.apply(sql.select(table.fields()).from(table))
                    .where(idAndPartition(id, partitionId))
                    .groupBy(keyField)
                    .fetch().stream().findFirst()
                    .map(mapper);
        }

        @Override
        public <T> Optional<T> readCustom(ID id, int partitionId, Function<Record, T> mapper,Field<?>[] customFields,
                                          Function<SelectJoinStep<Record>, SelectWhereStep<Record>> customQuery) {
            return customQuery.apply(sql.select(customFields).from(table))
                    .where(idAndPartition(id, partitionId))
                    .groupBy(keyField)
                    .fetch().stream().findFirst()
                    .map(mapper);
        }

        // CONSTRUCTORS

        private QueryMaker(DSLContext sql, Table<R> table, TableField<R, ID> keyField, TableField<R, Integer> partitionField) {
            this.sql = sql;
            this.table = table;
            this.keyField = keyField;
            this.partitionField = partitionField;
        }

        // PRIVATE

        private final DSLContext sql;
        private final Table<R> table;
        private final TableField<R, ID> keyField;
        private final TableField<R, Integer> partitionField;

        private Condition partitionId(int partitionId) {
            return partitionField.eq(partitionId);
        }

        private Condition idAndPartition(ID id, int partitionId) {
            return keyField.eq(id).and(partitionId(partitionId));
        }

    }

    @Transactional
    private static final class ForeignQueryMaker<ID> implements ForeignQueries<ID> {

        /**
         * @return true if a record with foreign key value id exists in given partition; false otherwise
         *
         * @throws NullPointerException if key is null
         */
        @Override
        public boolean isUsed(ID id) {
            NullChecker.check(id).notNull("Foreign id value cannot be null");

            return sql.fetchExists(
                    sql.selectOne()
                            .from(tables())
                            .where(foreignKeys.stream()
                                    .map(key -> key.eq(id))
                                    .reduce(Condition::or)
                                    .orElseThrow(() -> new AssertionError("At least one field should have been provided to the checker!"))));
        }

        // CONSTRUCTORS

        @SafeVarargs
        private ForeignQueryMaker(DSLContext sql, TableField<?, ID>... foreignKeys) {
            this.sql = sql;
            this.foreignKeys = Arrays.asList(foreignKeys);
        }

        // PRIVATE

        private final DSLContext sql;
        private final List<TableField<?, ID>> foreignKeys;

        private Table[] tables() {
            return foreignKeys.stream()
                    .map(TableField::getTable)
                    .toArray(Table[]::new);
        }

    }

}
