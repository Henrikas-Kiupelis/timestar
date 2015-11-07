package com.superum.api.v3.teacher.impl;

import com.superum.api.v3.teacher.Teacher;
import com.superum.api.v3.teacher.TeacherDeserializer;
import com.superum.api.v3.teacher.dto.SuppliedTeacher;
import org.springframework.stereotype.Service;

@Service
public class TeacherDeserializerImpl implements TeacherDeserializer {

    @Override
    public Teacher toCreatable(SuppliedTeacher fetchedTeacher) {
        return null;
    }

    @Override
    public Teacher toUpdatable(SuppliedTeacher suppliedTeacher) {
        return null;
    }

    // CONSTRUCTORS



    // PRIVATE



}
