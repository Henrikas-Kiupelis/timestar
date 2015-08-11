package com.superum.db.teacher;

import com.superum.TimeStarBackEndApplication;
import com.superum.db.account.AccountDAO;
import com.superum.db.partition.PartitionService;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.exception.DatabaseException;
import com.superum.helper.PartitionAccount;
import com.superum.helper.mail.GMail;
import com.superum.utils.FakeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.superum.utils.FakeUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TimeStarBackEndApplication.class})
public class TeacherServiceTests {

    private TeacherService teacherService;
    private TeacherDAO teacherDAO;
    private PasswordEncoder encoder;
    private GMail mail;
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
        mail = mock(GMail.class);
        teacherService = new TeacherServiceImpl(teacherDAO, encoder, mail, accountDAO, teacherLanguagesService, partitionService);
    }

    @Test
    public void testAddingTeacher() {
        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Teacher addedTeacher = makeFakeTeacher(id);
        Teacher originalTeacher = addedTeacher.withoutId();

        when(teacherDAO.create(originalTeacher, account.partitionId())).thenReturn(addedTeacher);

        Teacher retrievedTeacher = teacherService.addTeacher(originalTeacher, account);

        assertEquals("The added teacher should be the same as retrieved one;",
                addedTeacher, retrievedTeacher);

        verify(teacherDAO, times(1)).create(originalTeacher, account.partitionId());
    }

    @Test
    public void testFindingExistingTeacher() {
        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Teacher teacher = makeFakeTeacher(id);

        when(teacherDAO.read(id, account.partitionId())).thenReturn(teacher);

        Teacher retrievedTeacher = teacherService.findTeacher(id, account.partitionId());

        assertEquals("Original teacher should be the same as retrieved one;",
                teacher, retrievedTeacher);

        verify(teacherDAO, times(1)).read(id, account.partitionId());
    }

    @Test
    public void testDeletingTeacher() {
        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Teacher teacher = makeFakeTeacher(id);

        when(teacherDAO.delete(id, account.partitionId())).thenReturn(teacher);
        // TODO too few mocks!

        Teacher retrievedTeacher = teacherService.deleteTeacher(id, account);

        assertEquals("Original teacher should be the same as retrieved one;",
                teacher, retrievedTeacher);

        verify(teacherDAO, times(1)).delete(id, account.partitionId());

    }

    @Test
    public void testUpdatingExistingTeacherData() {
        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Teacher originalTeacher = makeFakeTeacher(id);

        Teacher updatedTeacher = makeFakeTeacher(id + 1).withId(id);

        when(teacherDAO.update(updatedTeacher, account.partitionId())).thenReturn(originalTeacher);

        Teacher retrievedTeacher = teacherService.updateTeacher(updatedTeacher, account.partitionId());

        assertEquals("Original teacher data should be the same as retrieved one;",
                originalTeacher, retrievedTeacher);

        verify(teacherDAO, times(1)).update(updatedTeacher, account.partitionId());
    }

    @Test(expected = DatabaseException.class)
    public void testUpdatingNonExistingTeacher() {
        int id = Integer.MAX_VALUE;
        PartitionAccount account = makeFakePartitionAccount();
        Teacher nonExistentTeacher = makeFakeTeacher(id);

        when(teacherDAO.update(nonExistentTeacher, account.partitionId())).thenThrow(new DatabaseException());

        teacherService.updateTeacher(nonExistentTeacher, account.partitionId());
    }

    @Test
    public void testGettingAllTeachers() {
        PartitionAccount account = makeFakePartitionAccount();
        List<Teacher> fakeTeachers = makeSomeFakes(2, FakeUtils::makeFakeTeacher);

        when(teacherDAO.readAll(account.partitionId())).thenReturn(fakeTeachers);

        List<Teacher> retrievedTeachers = teacherService.getAllTeachers(account.partitionId());

        assertEquals("Original teachers should be the same as retrieved ones;",
                fakeTeachers, retrievedTeachers);

        verify(teacherDAO, times(1)).readAll(account.partitionId());
    }

}
