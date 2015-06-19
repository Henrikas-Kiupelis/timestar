package com.superum.db.dao;

import java.util.List;

public interface FullAccessDAO<T> {

	List<T> readAll();
	
}
