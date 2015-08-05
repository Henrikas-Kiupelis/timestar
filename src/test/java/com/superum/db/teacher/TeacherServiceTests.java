package com.superum.db.teacher;

import com.superum.TimeStarBackEndApplication;
import com.superum.config.Gmail;
import com.superum.db.account.AccountDAO;
import com.superum.db.partition.PartitionService;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.exception.DatabaseException;
import com.superum.helper.utils.FakeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.superum.helper.utils.FakeUtils.makeFakeTeacher;
import static com.superum.helper.utils.FakeUtils.makeSomeFakes;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TimeStarBackEndApplication.class})
public class TeacherServiceTests {

    private TeacherService teacherService;
    private TeacherDAO teacherDAO;
    private PasswordEncoder encoder;
    private Gmail mail;
    private AccountDAO accountDAO;
    private TeacherLanguagesService teacherLanguagesService;
    private PartitionService partitionService;

    @Before
    public void init() {
        teacherDAO = mock(TeacherDAO.class);
        accountDAO = mock(AccountDAO.class);
        partitionService = mock(PartitionService.class);
        teacherLanguagesService = mock(TeacherLanguagesService.class);
        encoder = mock(PasswordEncoder.class);
        mail = mock(Gmail.class);
        teacherService = new TeacherServiceImpl(teacherDAO, encoder, mail, accountDAO, teacherLanguagesService, partitionService);
    }

    //+
    @Test
    public void testAddingTeacher() {
        int id = 1;
        int partitionId = 0;
        Teacher addedTeacher = makeFakeTeacher(id);
        Teacher originalTeacher = addedTeacher.withoutId();

        when(teacherDAO.create(originalTeacher, partitionId)).thenReturn(addedTeacher);

        Teacher retrievedTeacher = teacherDAO.create(originalTeacher, partitionId);

        assertEquals("The added teacher should be the same as retrieved one;",
                addedTeacher, retrievedTeacher);

        verify(teacherDAO, times(1)).create(originalTeacher, partitionId);
    }

    //+
    @Test
    public void testFindingExistingTeacher() {
        int id = 1;
        int partitionId = 0;
        Teacher teacher = makeFakeTeacher(id);

        when(teacherDAO.read(id, partitionId)).thenReturn(teacher);

        Teacher retrievedTeacher = teacherService.findTeacher(id, partitionId);

        assertEquals("Original teacher should be the same as retrieved one;",
                teacher, retrievedTeacher);

        verify(teacherDAO, times(1)).read(id, partitionId);
    }

    //+
    @Test
    public void testDeletingTeacher() {
        int id = 1;
        int partitionId = 0;
        Teacher teacher = makeFakeTeacher(id);

        when(teacherDAO.delete(id, partitionId)).thenReturn(teacher);

        Teacher retrievedTeacher = teacherService.deleteTeacher(id, partitionId);

        assertEquals("Original teacher should be the same as retrieved one;",
                teacher, retrievedTeacher);

        verify(teacherDAO, times(1)).delete(id, partitionId);

    }

    //+
    @Test
    public void testUpdatingExistingTeacherData() {
        int id = 1;
        int partitionId = 0;
        Teacher originalTeacher = makeFakeTeacher(id);

        Teacher updatedTeacher = makeFakeTeacher(id + 1).withId(id);

        when(teacherDAO.update(updatedTeacher, partitionId)).thenReturn(originalTeacher);

        Teacher retrievedTeacher = teacherService.updateTeacher(updatedTeacher, partitionId);

        assertEquals("Original teacher data should be the same as retrieved one;",
                originalTeacher, retrievedTeacher);

        verify(teacherDAO, times(1)).update(updatedTeacher, partitionId);
    }

    //+
    @Test(expected = DatabaseException.class)
    public void testUpdatingNonExistingTeacher() {
        int id = Integer.MAX_VALUE;
        int partitionId = 0;
        Teacher nonExistentTeacher = makeFakeTeacher(id);

        when(teacherDAO.update(nonExistentTeacher, partitionId)).thenThrow(new DatabaseException());

        teacherService.updateTeacher(nonExistentTeacher, partitionId);
    }

    @Test
    public void testGettingAllTeachers() {
        int partitionId = 0;
        List<Teacher> fakeTeachers = makeSomeFakes(2, FakeUtils::makeFakeTeacher);

        // I made the method we talked about :)
        // I also changed it to a JAVA 8 form now, just to show how nice it can be :)
        /*
        List<Teacher> allRandomTeachers = new ArrayList<>();
        allRandomTeachers.add(makeFakeTeacher(id, partitionId));
         */

        when(teacherDAO.readAll(partitionId)).thenReturn(fakeTeachers);

        List<Teacher> retrievedTeachers = teacherService.getAllTeachers(partitionId);

        assertEquals("Original teachers should be the same as retrieved ones;",
                fakeTeachers, retrievedTeachers);

        verify(teacherDAO, times(1)).readAll(partitionId);
    }

}
