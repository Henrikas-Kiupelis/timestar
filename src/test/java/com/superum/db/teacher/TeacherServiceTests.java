package com.superum.db.teacher;

import com.superum.TimeStarBackEndApplication;
import com.superum.config.Gmail;
import com.superum.db.account.AccountDAO;
import com.superum.db.account.roles.AccountRolesDAO;
import com.superum.db.exception.DatabaseException;
import com.superum.db.partition.PartitionService;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Test
    public void testCreatingAccount(){

    }



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

        Teacher nonExistantTeacherData = new Teacher(id, paymentDay, name, surname, phone, city, email, pictureName, documentName, comment);


        when(teacherDAO.update(nonExistantTeacherData, partitionId)).thenThrow(new DatabaseException());

        teacherService.updateTeacher(nonExistantTeacherData, partitionId);




    }



}
