package com.superum.db.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface SimpleDAO<T, ID> {

	T create(T t);
	T read(ID id);
	T update(T t);
	T delete(ID id);
	
}
