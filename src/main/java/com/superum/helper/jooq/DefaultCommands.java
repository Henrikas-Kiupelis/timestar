package com.superum.helper.jooq;

import com.superum.helper.field.Defined;
import org.jooq.Record;

import java.util.Optional;
import java.util.function.Function;

/**
 * Contains methods for commands on tables
 * @param <R> JOOQ generated table record for a table
 * @param <ID> primary key field type for this table
 */
public interface DefaultCommands<R extends Record, ID> extends DefaultSql<R, ID> {

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
    <T extends Defined<T, ID>, DTO> Optional<DTO> create(T body, int partitionId, Function<R, DTO> mapper);

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

}
