package com.superum.config;

import com.superum.api.customer.*;
import com.superum.db.account.*;
import com.superum.db.account.roles.AccountRolesDAO;
import com.superum.db.account.roles.AccountRolesDAOImpl;
import com.superum.db.customer.*;
import com.superum.db.customer.lang.*;
import com.superum.db.files.FileController;
import com.superum.db.files.FileService;
import com.superum.db.files.FileServiceImpl;
import com.superum.db.group.*;
import com.superum.db.group.student.*;
import com.superum.db.lesson.*;
import com.superum.db.lesson.attendance.*;
import com.superum.db.lesson.attendance.code.*;
import com.superum.db.lesson.table.*;
import com.superum.db.partition.*;
import com.superum.db.teacher.*;
import com.superum.db.teacher.lang.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Lazy
public class ApplicationConfig {

    @Bean
    public FullCustomerController fullCustomerController() {
        return new FullCustomerController(customerService(), fullCustomerService());
    }

    @Bean
    public FullCustomerService fullCustomerService() {
        return new FullCustomerServiceImpl(fullCustomerQueries());
    }

    @Bean
    public FullCustomerQueries fullCustomerQueries() {
        return new FullCustomerQueriesImpl(persistenceContext.dsl());
    }

    @Bean
    public AccountController accountController() {
        return new AccountController(accountService(), encoder);
    }

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl(accountDAO(), accountRolesDAO());
    }

    @Bean
    public AccountRolesDAO accountRolesDAO() {
        return new AccountRolesDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public AccountDAO accountDAO() {
        return new AccountDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public CustomerController customerController() {
        return new CustomerController(customerService());
    }

    @Bean
    public CustomerService customerService() {
        return new CustomerServiceImpl(customerDAO(), customerQueries(), customerLanguagesService(), studentService());
    }

    @Bean
    public StudentService studentService() {
        return new StudentServiceImpl(studentDAO(), studentQueries(), attendanceService(), lessonCodeService());
    }

    @Bean
    public LessonAttendanceService attendanceService() {
        return new LessonAttendanceServiceImpl(attendanceDAO(), lessonCodeService(), lessonCodeDAO(), studentDAO(), emailConfig.gmail(), partitionService());
    }

    @Bean
    public PartitionService partitionService() {
        return new PartitionServiceImpl(partitionDAO());
    }

    @Bean
    public PartitionDAO partitionDAO() {
        return new PartitionDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public LessonCodeService lessonCodeService() {
        return new LessonCodeServiceImpl(lessonCodeDAO());
    }

    @Bean
    public LessonCodeDAO lessonCodeDAO() {
        return new LessonCodeDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public LessonAttendanceDAO attendanceDAO() {
        return new LessonAttendanceDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public StudentQueries studentQueries() {
        return new StudentQueriesImpl(persistenceContext.dsl());
    }

    @Bean
    public StudentDAO studentDAO() {
        return new StudentDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public CustomerLanguagesService customerLanguagesService() {
        return new CustomerLanguagesServiceImpl(customerLanguagesDAO());
    }

    @Bean
    public CustomerLanguagesDAO customerLanguagesDAO() {
        return new CustomerLanguagesDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public CustomerQueries customerQueries() {
        return new CustomerQueriesImpl(persistenceContext.dsl());
    }

    @Bean
    public CustomerDAO customerDAO() {
        return new CustomerDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public CustomerLanguagesController customerLanguagesController() {
        return new CustomerLanguagesController(customerLanguagesService());
    }

    @Bean
    public FileController fileController() {
        return new FileController(fileService());
    }

    @Bean
    public FileService fileService() {
        return new FileServiceImpl();
    }

    @Bean
    public GroupController groupController() {
        return new GroupController(groupService());
    }

    @Bean
    public GroupService groupService() {
        return new GroupServiceImpl(groupDAO(), groupQueries(), studentService(), lessonService());
    }

    @Bean
    public LessonService lessonService() {
        return new LessonServiceImpl(lessonDAO(), lessonQueries(), lessonAttendanceService(), lessonCodeService());
    }

    @Bean
    public LessonAttendanceService lessonAttendanceService() {
        return new LessonAttendanceServiceImpl(lessonAttendanceDAO(), lessonCodeService(), lessonCodeDAO(), studentDAO(), emailConfig.gmail(), partitionService());
    }

    @Bean
    public LessonAttendanceDAO lessonAttendanceDAO() {
        return new LessonAttendanceDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public LessonQueries lessonQueries() {
        return new LessonQueriesImpl(persistenceContext.dsl());
    }

    @Bean
    public LessonDAO lessonDAO() {
        return new LessonDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public GroupQueries groupQueries() {
        return new GroupQueriesImpl(persistenceContext.dsl());
    }

    @Bean
    public GroupDAO groupDAO() {
        return new GroupDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public StudentController studentController() {
        return new StudentController(studentService());
    }

    @Bean
    public LessonController lessonController() {
        return new LessonController(lessonService());
    }

    @Bean
    public LessonCodeController lessonCodeController() {
        return new LessonCodeController(lessonCodeService());
    }

    @Bean
    public LessonAttendanceController lessonAttendanceController() {
        return new LessonAttendanceController(lessonAttendanceService());
    }

    @Bean
    public LessonTableController lessonTableController() {
        return new LessonTableController(lessonTableService());
    }

    @Bean
    public LessonTableService lessonTableService() {
        return new LessonTableServiceImpl(teacherDAO(), teacherLanguagesDAO(), customerDAO(), customerLanguagesDAO(), lessonTableQueries());
    }

    @Bean
    public LessonTableQueries lessonTableQueries() {
        return new LessonTableQueriesImpl(persistenceContext.dsl());
    }

    @Bean
    public TeacherLanguagesDAO teacherLanguagesDAO() {
        return new TeacherLanguagesDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public TeacherDAO teacherDAO() {
        return new TeacherDAOImpl(persistenceContext.dsl());
    }

    @Bean
    public PartitionController partitionController() {
        return new PartitionController(partitionService());
    }

    @Bean
    public TeacherController teacherController() {
        return new TeacherController(teacherService());
    }

    @Bean
    public TeacherService teacherService() {
        return new TeacherServiceImpl(teacherDAO(), encoder, emailConfig.gmail(), accountDAO(), accountRolesDAO(), teacherLanguagesService(), partitionService());
    }

    @Bean
    public TeacherLanguagesService teacherLanguagesService() {
        return new TeacherLanguagesServiceImpl(teacherLanguagesDAO());
    }

    @Bean
    public TeacherLanguagesController teacherLanguagesController() {
        return new TeacherLanguagesController(teacherLanguagesService());
    }

    // PRIVATE

    @Autowired
    private PersistenceContext persistenceContext;

    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private PasswordEncoder encoder;

}
