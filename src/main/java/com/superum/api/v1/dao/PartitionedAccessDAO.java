package com.superum.api.v1.dao;

import java.util.List;

public interface PartitionedAccessDAO<T> {

	List<T> readAll(int partitionId);
	
}
