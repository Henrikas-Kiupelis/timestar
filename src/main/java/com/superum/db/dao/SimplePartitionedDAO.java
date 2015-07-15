package com.superum.db.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface SimplePartitionedDAO<T, ID> {

	T create(T t, int partitionId);
	T read(ID id, int partitionId);
	T update(T t, int partitionId);
	T delete(ID id, int partitionId);
	
}
