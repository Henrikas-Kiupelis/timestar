package com.superum.db.dao;

public interface UpdatedSimplePartitionedDAO<T, ID> {

    T create(T t, int partitionId);
    T read(ID id, int partitionId);
    int update(T t, int partitionId);
    int delete(ID id, int partitionId);

}
