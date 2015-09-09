package com.superum.helper.jooq;

/**
 * Contains methods for commands on many-to-many tables
 * @param <Primary> one of the fields, which is usually less prevalent when creating; i.e. when creating
 *                 group-to-student relationship, group would be primary, because it is more likely that a lot of
 *                 students join a group, rather than a student joins a lot of groups
 * @param <Secondary> one of the fields, which is usually less prevalent when creating; using the above example,
 *                   student would be secondary
 */
public interface QueriesForMany<Primary, Secondary> {

    /**
     * @return true if a record with primary value exists; false otherwise
     * @throws NullPointerException if value is null
     */
    boolean existsPrimary(Primary value, int partitionId);

    /**
     * @return true if a record with secondary value exists; false otherwise
     * @throws NullPointerException if value is null
     */
    boolean existsSecondary(Secondary value, int partitionId);

}
