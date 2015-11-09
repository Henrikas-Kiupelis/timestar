package com.superum.api.v3.teacher.impl;

import com.superum.api.v2.teacher.UnsafeTeacherDeleteException;
import com.superum.api.v3.account.AccountServiceExt;
import com.superum.api.v3.teacher.Teacher;
import com.superum.api.v3.teacher.TeacherCommands;
import com.superum.api.v3.teacher.TeacherDeserializer;
import com.superum.api.v3.teacher.TeacherRepository;
import com.superum.api.v3.teacher.dto.FetchedTeacher;
import com.superum.api.v3.teacher.dto.SuppliedTeacher;
import com.superum.exception.DatabaseException;
import eu.goodlike.libraries.jooq.CommandsMany;
import eu.goodlike.libraries.jooq.Queries;
import eu.goodlike.libraries.jooq.QueriesForeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.TeacherRecord;

import static com.superum.api.v1.account.AccountType.TEACHER;
import static com.superum.api.v3.teacher.TeacherErrors.teacherIdError;

@Service
@Transactional
public class TeacherCommandsImpl implements TeacherCommands {

    @Override
    public FetchedTeacher create(SuppliedTeacher suppliedTeacher) {
        Teacher teacher = teacherDeserializer.toCreatable(suppliedTeacher);
        return teacher.create()
                .orElseThrow(() -> new DatabaseException("Couldn't create teacher: " + teacher));
    }

    @Override
    public void update(SuppliedTeacher suppliedTeacher, int id) {
        Teacher teacher = teacherDeserializer.toUpdatable(suppliedTeacher, id);
        if (teacher.update(id) == 0)
            throw new DatabaseException("Couldn't update teacher: " + teacher);
    }

    @Override
    public void delete(int id) {
        if (!teacherQueries.exists(id))
            throw teacherIdError(id);

        if (teacherForeignQueries.isUsed(id))
            throw new UnsafeTeacherDeleteException("Teacher with id " + id + " still has entries in other tables!");

        teacherLanguageCommands.deleteLeft(id);

        if (teacherRepository.delete(id) == 0)
            throw new DatabaseException("Couldn't delete teacher with id: " + id);

        accountServiceExt.deleteAccount(id, TEACHER);
    }

    // CONSTRUCTORS

    @Autowired
    public TeacherCommandsImpl(TeacherDeserializer teacherDeserializer,
                               Queries<TeacherRecord, Integer> teacherQueries,
                               QueriesForeign<Integer> teacherForeignQueries,
                               CommandsMany<Integer, String> teacherLanguageCommands,
                               TeacherRepository teacherRepository,
                               AccountServiceExt accountServiceExt) {
        this.teacherDeserializer = teacherDeserializer;
        this.teacherQueries = teacherQueries;
        this.teacherForeignQueries = teacherForeignQueries;
        this.teacherLanguageCommands = teacherLanguageCommands;
        this.teacherRepository = teacherRepository;
        this.accountServiceExt = accountServiceExt;
    }

    // PRIVATE

    private final TeacherDeserializer teacherDeserializer;
    private final Queries<TeacherRecord, Integer> teacherQueries;
    private final QueriesForeign<Integer> teacherForeignQueries;
    private final CommandsMany<Integer, String> teacherLanguageCommands;
    private final TeacherRepository teacherRepository;
    private final AccountServiceExt accountServiceExt;

}
