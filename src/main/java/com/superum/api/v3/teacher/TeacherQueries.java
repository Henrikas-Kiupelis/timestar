package com.superum.api.v3.teacher;

import com.superum.api.v3.teacher.dto.FetchedTeacher;

import java.util.List;

public interface TeacherQueries {

    FetchedTeacher readById(int id);

    List<FetchedTeacher> readAll(int page, int amount);

    int countAll();

}
