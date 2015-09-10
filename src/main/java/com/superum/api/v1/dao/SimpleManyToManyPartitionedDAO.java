package com.superum.api.v1.dao;

import java.util.List;

public interface SimpleManyToManyPartitionedDAO<ID, Field> {

    int create(ID id, List<Field> fields, int partitionId);
    List<Field> read(ID id, int partitionId);
    int delete(ID id, List<Field> fields, int partitionId);

}
