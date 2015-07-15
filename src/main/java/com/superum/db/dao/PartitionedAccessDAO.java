package com.superum.db.dao;

import java.util.List;

public interface PartitionedAccessDAO<T> {

	List<T> readAll(int partitionId);
	
}
