package com.superum.db.group;

import com.superum.TimeStarBackEndApplication;
import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentService;
import com.superum.db.lesson.Lesson;
import com.superum.db.lesson.LessonService;
import com.superum.helper.Fakes;
import com.superum.helper.PartitionAccount;
import com.superum.utils.FakeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static com.superum.utils.FakeUtils.*;
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
        PartitionAccount account = Fakes.partitionAccount();
        Group addedGroup = makeFakeGroup(id);
        Group originalGroup = addedGroup.withoutId();

        when(groupDAO.create(originalGroup, account.partitionId())).thenReturn(addedGroup);

        Group retrievedGroup = groupService.addGroup(originalGroup, account.partitionId());

        assertEquals("The added group should be the same as retrieved one",
                addedGroup, retrievedGroup);

        verify(groupDAO, times(1)).create(originalGroup, account.partitionId());
    }

    @Test
    public void testFindingGroup(){
        int id = 1;
        PartitionAccount account = Fakes.partitionAccount();
        Group group = makeFakeGroup(id);

        when(groupDAO.read(id, account.partitionId())).thenReturn(group);

        Group retrievedGroup = groupService.findGroup(id, account.partitionId());

        assertEquals("Original group should be the same as retrieved one",
                group, retrievedGroup);

        verify(groupDAO, times(1)).read(id, account.partitionId());
    }

    @Test
    public void testUpdatingGroup(){
        int id = 1;
        PartitionAccount account = Fakes.partitionAccount();
        Group originalGroup = makeFakeGroup(id);
        Group updatedGroup = makeFakeGroup(id + 1).withId(id);

        when(groupDAO.update(updatedGroup, account.partitionId())).thenReturn(originalGroup);

        Group retrievedGroup = groupService.updateGroup(updatedGroup, account.partitionId());

        assertEquals("Original group should be the same as retrieved one",
                originalGroup, retrievedGroup);

        verify(groupDAO, times(1)).update(updatedGroup, account.partitionId());

    }

    @Test
    public void testDeletingGroup(){
        int id = 1;
        PartitionAccount account = Fakes.partitionAccount();
        Group group = makeFakeGroup(id);
        List<Student> fakeStudents = makeSomeFakes(2, FakeUtils::makeFakeStudent);
        List<Lesson> fakeLessons = makeSomeFakesLong(2, FakeUtils::makeFakeLesson);

        when(studentService.deleteForGroup(id, account.partitionId())).thenReturn(fakeStudents);
        when(lessonService.deleteForGroup(id, account.partitionId())).thenReturn(fakeLessons);
        when(groupDAO.delete(id, account.partitionId())).thenReturn(group);

        Group retrievedGroup = groupService.deleteGroup(id, account.partitionId());

        assertEquals("Original group should be the same as retrieved one",
                group, retrievedGroup);

        verify(studentService, times(1)).deleteForGroup(id, account.partitionId());
        verify(lessonService, times(1)).deleteForGroup(id, account.partitionId());
        verify(groupDAO, times(1)).delete(id, account.partitionId());
    }

    @Test
    public void testFindingGroupForCustomer(){
        int customerId = 1;
        PartitionAccount account = Fakes.partitionAccount();
        List<Group> fakeGroups = Collections.singletonList(makeFakeGroup(customerId));

        when(groupDAO.readAllForCustomer(customerId, account.partitionId())).thenReturn(fakeGroups);

         List<Group> retrievedGroups = groupService.findGroupsForCustomer(customerId, account.partitionId());

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).readAllForCustomer(customerId, account.partitionId());
    }

    @Test
    public void testFindingGroupForTeacher(){
        int teacherId = 1;
        PartitionAccount account = Fakes.partitionAccount();
        List<Group> fakeGroups = Collections.singletonList(makeFakeGroup(teacherId));

        when(groupDAO.readAllForTeacher(teacherId, account.partitionId())).thenReturn(fakeGroups);

        List<Group> retrievedGroups = groupService.findGroupsForTeacher(teacherId, account.partitionId());

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).readAllForTeacher(teacherId, account.partitionId());
    }

    @Test
    public void testFindingGroupForCustomerAndTeacher(){
        int teacherId = 1;
        int customerId = 1;
        PartitionAccount account = Fakes.partitionAccount();
        List<Group> fakeGroups = Collections.singletonList(makeFakeGroup(customerId));

        when(groupDAO.readAllForCustomerAndTeacher(customerId, teacherId, account.partitionId())).thenReturn(fakeGroups);

        List<Group> retrievedGroups = groupService.findGroupsForCustomerAndTeacher(customerId, teacherId, account.partitionId());

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).readAllForCustomerAndTeacher(customerId, teacherId, account.partitionId());
    }

    @Test
    public void testAll() {
        PartitionAccount account = Fakes.partitionAccount();
        List<Group> fakeGroups = makeSomeFakes(2, FakeUtils::makeFakeGroup);

        when(groupDAO.all(account.partitionId())).thenReturn(fakeGroups);

        List<Group> retrievedGroups = groupService.all(account.partitionId());

        assertEquals("Fake groups should be the same as retrieved ones",
                fakeGroups, retrievedGroups);

        verify(groupDAO, times(1)).all(account.partitionId());
    }

}
