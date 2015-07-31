package com.superum.db.teacher;

import com.superum.TimeStarBackEndApplication;
import com.superum.config.Gmail;
import com.superum.db.account.AccountDAO;
import com.superum.db.partition.PartitionService;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.exception.DatabaseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.superum.utils.FakeUtils.makeFakeTeacher;
import static com.superum.utils.FakeUtils.makeSomeFakeTeachers;
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

        // Instead of manually doing the same thing multiple times, let's create a method Teacher::withoutId
        // and while we're at it, Teacher::withId, which will save a lot of time :)
        /*
        int paymentDay = addedTeacher.getPaymentDay();
        BigDecimal hourlyWage = addedTeacher.getHourlyWage();
        BigDecimal academicWage = addedTeacher.getAcademicWage();
        String name = addedTeacher.getName();
        String surname = addedTeacher.getSurname();
        String phone = addedTeacher.getPhone();
        String city = addedTeacher.getCity();
        String picture = addedTeacher.getPicture();
        String document = addedTeacher.getDocument();
        String comment = addedTeacher.getComment();
        String email = addedTeacher.getEmail();

        Teacher originalTeacher = new Teacher(0, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment);
        */

        // There are too many teachers here, the fake one is just fine :)
        // Delete the commented parts after you check them
        /*
        int addedPaymentDay = 1;
        String addedName = "Fake Name";
        String addedSurname = "Fake Surname";
        String addedPhone = "Too few numbers";
        String addeedCity = "Non Existant City";
        String addedPictureName = "None Picture Name";
        String addedDocumentName = "None Document Name";
        String addedComment = "None comment";
        String addedEmail = "Not Existant Email";

        Teacher addedTeacher = new Teacher(id, addedPaymentDay, addedName, addedSurname, addedPhone, addeedCity, addedEmail, addedPictureName, addedDocumentName, addedComment);
        */

        when(teacherDAO.create(originalTeacher, partitionId)).thenReturn(addedTeacher);

        Teacher retrievedTeacher = teacherDAO.create(originalTeacher, partitionId);

        assertEquals("The added teacher should be the same as retrieved one;",
                addedTeacher, retrievedTeacher);

        verify(teacherDAO, times(1)).create(originalTeacher, partitionId);

        // The teacher that will be sent to us should not have id set - id == 0
        // The returned one, however, should have id set
        /*
        when(teacherDAO.create(addedTeacher, partitionId)).thenReturn(originalTeacher);

        Teacher retrievedTeacher = teacherDAO.create(addedTeacher, partitionId);

        assertEquals("Original teacher should be the same as retrieved one;",
                originalTeacher, retrievedTeacher);

        verify(teacherDAO, times(1)).create(addedTeacher, partitionId);
        */
    }

    //+
    @Test
    public void testFindingExistingTeacher() {
        int id = 1;
        int partitionId = 0;
        // You should not be calling mocked objects, only the object we are testing - teacherService
        /*
        Teacher teacher = teacherDAO.read(id, partitionId);
        */
        Teacher teacher = makeFakeTeacher(id);

        when(teacherDAO.read(id, partitionId)).thenReturn(teacher);
        // I believe we spoke about what's wrong here already :)
        /*
        when(teacher).thenReturn(teacher1);
        */

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
        // Same as above
        /*
        Teacher teacher1 = teacherDAO.delete(id, partitionId);
        */

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

        // Same as above
        /*
        int paymentDay = teacher.getPaymentDay();
        BigDecimal hourlyWage = addedTeacher.getHourlyWage();
        BigDecimal academicWage = addedTeacher.getAcademicWage();
        String name = addedTeacher.getName();
        String surname = addedTeacher.getSurname();
        String phone = addedTeacher.getPhone();
        String city = addedTeacher.getCity();
        String picture = addedTeacher.getPicture();
        String document = addedTeacher.getDocument();
        String comment = addedTeacher.getComment();
        String email = addedTeacher.getEmail();

        Teacher originalTeacherData = new Teacher(id, paymentDay, name, surname, phone, city, email, pictureName, documentName, comment);
        */

        Teacher updatedTeacher = makeFakeTeacher(id + 1).withId(id);

        // Since all the fields depend on id, might as well make a teacher like this, then change its id
        /*
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
        */

        when(teacherDAO.update(updatedTeacher, partitionId)).thenReturn(originalTeacher);

        Teacher retrievedTeacherData = teacherService.updateTeacher(updatedTeacher, partitionId);

        assertEquals("Original teacher data should be the same as retrieved one;",
                originalTeacher, retrievedTeacherData);

        // This was incorrect - updatedTeacher is the not the original one :)
        /*
        assertEquals("Original teacher data should be the same as retrieved one;",
                updatedTeacher, retrievedTeacherData);
         */

        verify(teacherDAO, times(1)).update(updatedTeacher, partitionId);
    }

    //+
    @Test(expected = DatabaseException.class)
    public void testUpdatingNonExistingTeacher() {
        // I like to use Integer.MAX_VALUE and similar to indicate that such an id cannot exist (if it did, the database
        // would essentially be full and crash if another line was added to it -> therefore, a switch to long ids would be needed :)
        int id = Integer.MAX_VALUE;
        int partitionId = 0;
        Teacher nonExistentTeacher = makeFakeTeacher(id);

        // It is not necessary to have every field use advanced values :)
        // Plus, keep in mind, some values are invalid, i.e. Byte.MAX_VALUE would not work, because it checks internally
        // that paymentDay would be between 1 and 31;
        /*
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
        */

        when(teacherDAO.update(nonExistentTeacher, partitionId)).thenThrow(new DatabaseException());

        teacherService.updateTeacher(nonExistentTeacher, partitionId);
    }

    @Test
    public void testGettingAllTeachers() {
        int id = 1;
        int partitionId = 0;
        List<Teacher> fakeTeachers = makeSomeFakeTeachers(2);

        // I made the method we talked about :)
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
