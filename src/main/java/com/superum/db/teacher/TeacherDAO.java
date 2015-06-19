package com.superum.db.teacher;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.FullAccessDAO;
import com.superum.db.dao.SimpleDAO;

@Repository
public interface TeacherDAO extends SimpleDAO<Teacher, Integer>, FullAccessDAO<Teacher> {}
