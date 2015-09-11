package com.superum.api.v2.teacher;

import com.superum.helper.jooq.DefaultQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.TeacherRecord;

import java.util.List;

@Service
public class ValidTeacherQueryServiceImpl implements ValidTeacherQueryService {

    @Override
    public FullTeacherDTO readById(int teacherId, int partitionId) {
        return teachers.forId(teacherId, partitionId)
                .orElseThrow(() -> new TeacherNotFoundException("Couldn't find teacher with id " + teacherId));
    }

    @Override
    public List<FullTeacherDTO> readAll(int page, int amount, int partitionId) {
        return teachers.all(page, amount, partitionId);
    }

    @Override
    public int countAll(int partitionId) {
        return defaultTeacherQueries.countAll(partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidTeacherQueryServiceImpl(TeacherFetcher teachers,
                                        DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries) {
        this.teachers = teachers;
        this.defaultTeacherQueries = defaultTeacherQueries;
    }

    // PRIVATE

    private final TeacherFetcher teachers;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;

}
