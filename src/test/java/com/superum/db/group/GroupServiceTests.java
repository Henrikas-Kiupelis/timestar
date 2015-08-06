package com.superum.db.group;


import com.superum.TimeStarBackEndApplication;
import com.superum.db.group.student.StudentService;
import com.superum.db.lesson.LessonService;
import com.superum.helper.utils.FakeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.superum.helper.utils.FakeUtils.makeFakeGroup;
import static com.superum.helper.utils.FakeUtils.makeSomeFakes;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TimeStarBackEndApplication.class})

public class GroupServiceTests {

    private GroupService groupService;
    private GroupDAO groupDAO;
    private StudentService studentService;
    private LessonService lessonService;

    @Before
    public void init() {
        groupDAO = mock(GroupDAO.class);
        studentService = mock(StudentService.class);
        lessonService = mock(LessonService.class);

        groupService = new GroupServiceImpl(groupDAO, studentService, lessonService);

    }

    @Test
    public void testAddingGroup(){
        int id = 1;
        int partitionId = 0;
        Group addedGroup = makeFakeGroup(id);
        Group originalGroup = addedGroup.withoutId();

        when(groupDAO.create(originalGroup, partitionId)).thenReturn(addedGroup);

        Group retrievedGroup = groupDAO.create(originalGroup, partitionId);

        assertEquals("The added group should be the same as retrieved one",
                addedGroup, retrievedGroup);

        verify(groupDAO, times(1)).create(originalGroup, partitionId);


    }

    @Test
    public void testFindingGroup(){
        int id = 1;
        int partitionId = 0;
        Group group = makeFakeGroup(id);

        when(groupDAO.read(id, partitionId)).thenReturn(group);

        Group retrievedGroup = groupService.findGroup(id, partitionId);

        assertEquals("Original group should be the same as retrieved one",
                group, retrievedGroup);

        verify(groupDAO, times(1)).read(id, partitionId);

    }

    @Test
    public void testUpdatingGroup(){
        int id = 1;
        int partitionId = 0;
        Group originalGroup = makeFakeGroup(id);
        Group updatedGroup = makeFakeGroup(id + 1).withId(id);

        when(groupDAO.update(updatedGroup, partitionId)).thenReturn(originalGroup);

        Group retrievedGroup = groupService.updateGroup(updatedGroup, partitionId);

        assertEquals("Original group should be the same as retrieved one",
                originalGroup, retrievedGroup);

        verify(groupDAO, times(1)).update(updatedGroup, partitionId);
    }

    @Test
    public void testDeletingGroup(){
        int id = 1;
        int partitionId = 0;
        Group group = makeFakeGroup(id);

        when(groupDAO.delete(id, partitionId)).thenReturn(group);

        Group retrievedGroup = groupService.deleteGroup(id, partitionId);

        assertEquals("Original group should be the same as retrieved one",
                group, retrievedGroup);

        verify(groupDAO, times(1)).delete(id, partitionId);

    }

    @Test
    public void testFindingGroupForCustomer(){
        int customerId = 1;
        int partitionId = 0;
        List<Group> fakeGroups = makeSomeFakes(2, FakeUtils::makeFakeGroup);

        when(groupDAO.readAllForCustomer(customerId, partitionId)).thenReturn(fakeGroups);

         List<Group> retrievedGroups = groupService.findGroupsForCustomer(customerId, partitionId);

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).readAllForCustomer(customerId, partitionId);

    }

    @Test
    public void testFindingGroupForTeacher(){
        int teacherId = 1;
        int partitionId = 0;
        List<Group> fakeGroups = makeSomeFakes(2, FakeUtils::makeFakeGroup);

        when(groupDAO.readAllForTeacher(teacherId, partitionId)).thenReturn(fakeGroups);

        List<Group> retrievedGroups = groupService.findGroupsForTeacher(teacherId, partitionId);

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).readAllForTeacher(teacherId, partitionId);

    }

    @Test
    public void testFindingGroupForCustomerAndTeacher(){
        int teacherId = 1;
        int customerId = 1;
        int partitionId = 0;
        List<Group> fakeGroups = makeSomeFakes(2, FakeUtils::makeFakeGroup);

        when(groupDAO.readAllForCustomerAndTeacher(customerId, teacherId, partitionId)).thenReturn(fakeGroups);

        List<Group> retrievedGroups = groupService.findGroupsForCustomerAndTeacher(customerId, teacherId, partitionId);

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).readAllForCustomerAndTeacher(customerId, teacherId, partitionId);

    }

    @Test
    public void testAll() {
        int partitionId = 0;
        List<Group> fakeGroups = makeSomeFakes(2, FakeUtils::makeFakeGroup);

        when(groupDAO.all(partitionId)).thenReturn(fakeGroups);

        List<Group> retrievedGroups = groupService.all(partitionId);

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).all(partitionId);

    }





}
