package com.superum.db.group.studentsgroup;

import com.superum.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class StudentsInGroupServiceImpl implements StudentsInGroupService {

    @Override
    public StudentsInGroup addStudentsToGroup(StudentsInGroup studentsInGroup, int partitionId) {
        LOG.debug("Adding students to group: {}", studentsInGroup);

        try {
            int createResult = studentsInGroupDAO.create(studentsInGroup.getGroupId(), studentsInGroup.getStudentIds(), partitionId);
            if (createResult == 0)
                throw new DatabaseException("Couldn't add students to group: " + studentsInGroup);
        } catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert students to group: " + studentsInGroup +
                    "; please refer to the nested exception for more info.", e);
        }
        LOG.debug("Students added to group: {}", studentsInGroup);

        return studentsInGroup;
    }

    @Override
    public StudentsInGroup getStudentsInGroup(int groupId, int partitionId) {
        LOG.debug("Reading students for group with id: {}", groupId);

        List<Integer> studentIds;
        try {
            studentIds = studentsInGroupDAO.read(groupId, partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read students in group with id: " + groupId, e);
        }
        StudentsInGroup studentsInGroup = new StudentsInGroup(groupId, studentIds);
        LOG.debug("Students in group retrieved: {}", studentsInGroup);

        return studentsInGroup;
    }

    @Override
    public StudentsInGroup updateStudentsInGroup(StudentsInGroup studentsInGroup, int partitionId) {
        LOG.debug("Updating students in group: {}", studentsInGroup);

        int groupId = studentsInGroup.getGroupId();
        StudentsInGroup old = deleteStudentsInGroup(groupId, partitionId);
        LOG.debug("Students in group before update: {}");

        try {
            studentsInGroupDAO.create(groupId, studentsInGroup.getStudentIds(), partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update students in group: " + studentsInGroup, e);
        }
        LOG.debug("Students in group updated: {}", studentsInGroup);

        return old;
    }

    @Override
    public StudentsInGroup deleteStudentsInGroup(int groupId, int partitionId) {
        return deleteStudentsInGroup(new StudentsInGroup(groupId, Collections.emptyList()), partitionId);
    }

    @Override
    public StudentsInGroup deleteStudentsInGroup(StudentsInGroup studentsInGroup, int partitionId) {
        LOG.debug("Deleting students from group: {}", studentsInGroup);

        int groupId = studentsInGroup.getGroupId();
        StudentsInGroup old = getStudentsInGroup(groupId, partitionId);
        LOG.debug("Students in group before deletion: {}", old);

        try {
            studentsInGroupDAO.delete(groupId, studentsInGroup.getStudentIds(), partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete students from group: " + studentsInGroup, e);
        }
        LOG.debug("Students deleted from group: {}", studentsInGroup);

        return old;
    }

    // CONSTRUCTORS

    @Autowired
    public StudentsInGroupServiceImpl(StudentsInGroupDAO studentsInGroupDAO) {
        this.studentsInGroupDAO = studentsInGroupDAO;
    }

    // PRIVATE

    private final StudentsInGroupDAO studentsInGroupDAO;

    private static final Logger LOG = LoggerFactory.getLogger(StudentsInGroupService.class);

}
