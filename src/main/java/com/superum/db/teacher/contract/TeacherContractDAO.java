package com.superum.db.teacher.contract;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface TeacherContractDAO extends SimpleDAO<TeacherContract, Integer> {}
