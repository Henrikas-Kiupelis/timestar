package com.superum.db.teacher;

import com.superum.TimeStarBackEndApplication;
import com.superum.config.Gmail;
import com.superum.db.account.AccountDAO;
import com.superum.db.account.roles.AccountRolesDAO;
import com.superum.db.partition.PartitionService;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.exception.DatabaseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static com.superum.utils.FakeUtils.makeFakeTeacher;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

//import com.superum.db.exception.DatabaseException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TimeStarBackEndApplication.class})
public class TeacherServiceTests {


    private TeacherService teacherService;
    private TeacherDAO teacherDAO;
    private PasswordEncoder encoder;
    private Gmail mail;
    private AccountDAO accountDAO;
    private AccountRolesDAO accountRolesDAO;
    private TeacherLanguagesService teacherLanguagesService;
    private PartitionService partitionService;

    @Before
    public void init() {
        teacherDAO = mock(TeacherDAO.class);
        accountDAO = mock(AccountDAO.class);
        accountRolesDAO = mock(AccountRolesDAO.class);
        partitionService = mock(PartitionService.class);
        teacherLanguagesService = mock(TeacherLanguagesService.class);
        encoder = mock(PasswordEncoder.class);
        mail = mock(Gmail.class);
        teacherService = new TeacherServiceImpl(teacherDAO, encoder, mail, accountDAO, accountRolesDAO, teacherLanguagesService, partitionService);
    }

    //+
    @Test
    public void testAddingTeacher(){
        int id =1;
        int partitionId = Integer.MAX_VALUE;
        Teacher teacher = makeFakeTeacher(id, partitionId);
        byte paymentDay = teacher.getPaymentDay();
        String name = teacher.getName();
        String surname = teacher.getSurname();
        String phone = teacher.getPhone();
        String city = teacher.getCity();
        String pictureName = teacher.getPictureName();
        String documentName = teacher.getDocumentName();
        String comment = teacher.getComment();
        String email = teacher.getEmail();

        Teacher originalTeacher = new Teacher(id, paymentDay, name, surname, phone, city, email, pictureName, documentName, comment);

        byte addedPaymentDay = Byte.MAX_VALUE;
        String addedName = "Fake Name";
        String addedSurname = "Fake Surname";
        String addedPhone = "Too few numbers";
        String addeedCity = "Non Existant City";
        String addedPictureName = "None Picture Name";
        String addedDocumentName = "None Document Name";
        String addedComment = "None comment";
        String addedEmail = "Not Existant Email";

        Teacher addedTeacher = new Teacher(id, addedPaymentDay, addedName, addedSurname, addedPhone, addeedCity, addedEmail, addedPictureName, addedDocumentName, addedComment);

        when(teacherDAO.create(addedTeacher, partitionId)).thenReturn(originalTeacher);

        Teacher retrievedTeacher = teacherDAO.create(addedTeacher, partitionId);

        assertEquals("Original teacher should be the same as retrieved one;",
                originalTeacher, retrievedTeacher);

        verify(teacherDAO, times(1)).create(addedTeacher, partitionId);

    }


    //+
    @Test
    public void testFindingExistingTeacher() {
        int id = 1;
        int partitionId = Integer.MAX_VALUE;
        Teacher teacher = teacherDAO.read(id, partitionId);
        Teacher teacher1 = makeFakeTeacher(id, partitionId);

        when(teacher).thenReturn(teacher1);

        Teacher retrievedTeacher = teacherService.findTeacher(id, partitionId);


        assertEquals("Original teacher should be the same as retrieved one;",
                teacher1, retrievedTeacher);

        verify(teacherDAO, times(1)).read(id, partitionId);
    }

    //+
    @Test
    public void testDeletingTeacher(){
        int id = 1;
        int partitionId = Integer.MAX_VALUE;
        Teacher teacher = makeFakeTeacher(id, partitionId);
        Teacher teacher1 = teacherDAO.delete(id, partitionId);

        when(teacher1).thenReturn(teacher);
        Teacher retrievedTeacher = teacherService.deleteTeacher(id, partitionId);

        assertEquals("Original teacher should be the same as retrieved one;",
                teacher, retrievedTeacher);

        verify(teacherDAO, times(1)).delete(id, partitionId);

    }

    //+
    @Test
    public void testUpdatingExistingTeacherData() {
        int id = 1;
        int partitionId = Integer.MAX_VALUE;
        Teacher teacher = makeFakeTeacher(id, partitionId);
        byte paymentDay = teacher.getPaymentDay();
        String name = teacher.getName();
        String surname = teacher.getSurname();
        String phone = teacher.getPhone();
        String city = teacher.getCity();
        String pictureName = teacher.getPictureName();
        String documentName = teacher.getDocumentName();
        String comment = teacher.getComment();
        String email = teacher.getEmail();

        Teacher originalTeacherData = new Teacher(id, paymentDay, name, surname, phone, city, email, pictureName, documentName, comment);

        byte updatedPaymentDay = Byte.MAX_VALUE;
        String updatedName = "Fake Name";
        String updatedSurname = "Fake Surname";
        String updatedPhone = "Too few numbers";
        String updatedCity = "Non Existant City";
        String updatedPictureName = "None Picture Name";
        String updatedDocumentName = "None Document Name";
        String updatedComment = "None comment";
        String updatedEmail = "Not Existant Email";

        Teacher updatedTeacherData = new Teacher(id, updatedPaymentDay, updatedName, updatedSurname, updatedPhone, updatedCity, updatedEmail, updatedPictureName, updatedDocumentName, updatedComment);

        when(teacherDAO.update(updatedTeacherData, partitionId)).thenReturn(originalTeacherData);

        Teacher retrievedTeacherData = teacherService.updateTeacher(updatedTeacherData, partitionId);

        assertEquals("Original teacher data should be the same as retrieved one;",
                originalTeacherData, retrievedTeacherData);

        verify(teacherDAO, times(1)).update(updatedTeacherData, partitionId);
    }

    //+
    @Test(expected = DatabaseException.class)
    public void testUpdatingNonExistingTeacher() {
        int id = 1;
        int partitionId = Integer.MAX_VALUE;
        byte paymentDay = Byte.MAX_VALUE;
        String name = "Non Existant";
        String surname = "Person";
        String phone = "Not Given";
        String city = "Not Existant City";
        String email = "Not Existant Email";
        String pictureName = "Not Existant Picture Name";
        String documentName = "Not Existant Document Name";
        String comment = "Not Existant Comment";

        Teacher nonExistentTeacherData = new Teacher(id, paymentDay, name, surname, phone, city, email, pictureName, documentName, comment);


        when(teacherDAO.update(nonExistentTeacherData, partitionId)).thenThrow(new DatabaseException());

        teacherService.updateTeacher(nonExistentTeacherData, partitionId);


    }

    @Test
    public void testGettingAllTeachers(){
        int id = 1;
        int partitionId = 0;
        List<Teacher> allRandomTeachers = new ArrayList<>();
        allRandomTeachers.add(makeFakeTeacher(id, partitionId));

        when(teacherDAO.readAll(partitionId)).thenReturn(allRandomTeachers);
        List<Teacher> retrievedTeacher = teacherService.getAllTeachers(partitionId);

        assertEquals("Original teacher should be the same as retrieved one;",
                allRandomTeachers, retrievedTeacher);

        verify(teacherDAO, times(1)).readAll(partitionId);



    }





}
