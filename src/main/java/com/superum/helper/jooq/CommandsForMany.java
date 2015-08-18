package com.superum.helper.jooq;

import com.superum.helper.field.ManyDefined;

/**
 * Contains methods for commands on many-to-many tables
 * @param <Primary> one of the fields, which is usually less prevalent when creating; i.e. when creating
 *                 group-to-student relationship, group would be primary, because it is more likely that a lot of
 *                 students join a group, rather than a student joins a lot of groups
 * @param <Secondary> one of the fields, which is usually less prevalent when creating; using the above example,
 *                   student would be secondary
 */
public interface CommandsForMany<Primary, Secondary> {

    /**
     * <pre>
     * Creates records from a many-to-many object
     *
     * Uses the definitions in parameter 'bodyOfMany' to fill the fields
     * </pre>
     * @return amount of records created
     */
    <T extends ManyDefined<Primary, Secondary>> int create(T bodyOfMany, int partitionId);

    /**
     * <pre>
     * Deletes records using a many-to-many object
     *
     * Uses the definitions in parameter 'bodyOfMany' to choose which fields to delete
     * </pre>
     * @return amount of records deleted
     */
    <T extends ManyDefined<Primary, Secondary>> int delete(T bodyOfMany, int partitionId);

    /**
     * Deletes records with a given Primary value
     * @return amount of records deleted
     */
    int deletePrimary(Primary value, int partitionId);

    /**
     * Deletes records with a given Secondary value
     * @return amount of records deleted
     */
    int deleteSecondary(Secondary value, int partitionId);

    /**
     * <pre>
     * Updates records using a many-to-many object
     *
     * Essentially, all records for Primary value are deleted, then all the records in 'bodyOfMany' are created
     * </pre>
     * @return amount of records created when updating
     */
    default <T extends ManyDefined<Primary, Secondary>> int update(T bodyOfMany, int partitionId) {
        deletePrimary(bodyOfMany.primaryValue(), partitionId);
        return create(bodyOfMany, partitionId);
    }

}
