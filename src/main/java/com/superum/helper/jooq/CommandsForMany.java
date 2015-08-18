package com.superum.helper.jooq;

import com.superum.helper.field.ManyDefined;

public interface CommandsForMany<Primary, Secondary> {

    <T extends ManyDefined<Primary, Secondary>> int create(T bodyOfMany, int partitionId);

    <T extends ManyDefined<Primary, Secondary>> int delete(T bodyOfMany, int partitionId);

    int deletePrimary(Primary value, int partitionId);

    int deleteSecondary(Secondary value, int partitionId);

    default <T extends ManyDefined<Primary, Secondary>> int update(T bodyOfMany, int partitionId) {
        deletePrimary(bodyOfMany.primaryValue(), partitionId);
        return create(bodyOfMany, partitionId);
    }

}
