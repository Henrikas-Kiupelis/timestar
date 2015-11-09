package com.superum.helper;

import com.superum.TimeStarBackEndApplication;
import com.superum.config.DefaultSqlConfig;
import com.superum.config.DefaultSqlConfigV3;
import com.superum.config.PersistenceContext;
import com.superum.config.SecurityConfig;
import eu.goodlike.libraries.spring.mockmvc.MVC;
import eu.goodlike.libraries.spring.mockmvc.MVCMock;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.superum.helper.TestConstants.TEST_PARTITION;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TimeStarBackEndApplication.class, PersistenceContext.class, SecurityConfig.class,
        HelperConfiguration.class, MockConfig.class, DefaultSqlConfig.class, DefaultSqlConfigV3.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public abstract class IntegrationTestEnvironment {

    @Before
    public void setup() throws NamingException {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain)
                .build();
        this.mvc = MVC.mock(mockMvc).withAuth(AUTH_TOKEN);
        db.init();
    }

    // PROTECTED

    protected MVCMock mvc;

    @Autowired
    protected DB db;

    protected <T, ID> void assertNotInDatabase(BiFunction<DB, ID, Optional<T>> dbGetter, ID id) {
        Optional<T> valueInDb = dbGetter.apply(db, id);
        assertFalse("Value should not be in database", valueInDb.isPresent());
    }

    protected <T, ID> void assertInDatabase(BiFunction<DB, ID, Optional<T>> dbGetter, Function<T, ID> idGetter, T t) {
        Optional<T> valueInDb = dbGetter.apply(db, idGetter.apply(t));
        assertTrue("Value should be in database", valueInDb.isPresent());
        assertEquals("Value in database should be equal to the given value", t, valueInDb.get());
    }

    protected <T, ID> void assertInDatabase(BiFunction<DB, ID, Optional<T>> dbGetter, Function<T, ID> idGetter,
                                          T t, BiPredicate<T, T> customEqualityCheck) {
        Optional<T> valueInDb = dbGetter.apply(db, idGetter.apply(t));
        assertTrue("Value should be in database", valueInDb.isPresent());
        assertTrue("Value in database should be equal to the given value; " +
                        "expected: " + t + ", found: " + valueInDb.get(),
                customEqualityCheck.test(t, valueInDb.get()));
    }

    protected <T, ID> void assertInDatabase(BiFunction<DB, ID, Optional<T>> dbGetter, ID id) {
        Optional<T> valueInDb = dbGetter.apply(db, id);
        assertTrue("Value should be in database", valueInDb.isPresent());
    }

    protected <ID> void assertInDatabaseSecondary(BiPredicate<DB, ID> existsSecondary, ID id) {
        assertTrue("Secondary ID should be in the database", existsSecondary.test(db, id));
    }

    protected <ID> void assertNotInDatabaseSecondary(BiPredicate<DB, ID> existsSecondary, ID id) {
        assertFalse("Secondary ID should not be in the database", existsSecondary.test(db, id));
    }

    protected int readCount(MvcResult result) throws IOException {
        return MVC.from(result).to(Integer.class);
    }

    protected static final int OLD_TEACHER_ID = 1;
    protected static final int OLD_CUSTOMER_ID = 1;
    protected static final int OLD_GROUP_ID = 1;
    protected static final int OLD_STUDENT_ID = 1;
    protected static final long OLD_LESSON_ID = 1;

    protected static final int EXTRA_TEACHER_ID = 2;
    protected static final int EXTRA_CUSTOMER_ID = 2;
    protected static final int EXTRA_GROUP_ID = 2;
    protected static final int EXTRA_STUDENT_ID = 2;
    protected static final long EXTRA_LESSON_ID = 2;

    protected static final int NEW_TEACHER_ID = 10;
    protected static final int NEW_CUSTOMER_ID = 10;
    protected static final int NEW_GROUP_ID = 10;
    protected static final int NEW_STUDENT_ID = 10;
    protected static final long NEW_LESSON_ID = 10;

    // PRIVATE

    @Autowired
    private WebApplicationContext wac;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    private static final String USERNAME = TEST_PARTITION + ".test";
    private static final String PASSWORD = "test";
    private static final byte[] TOKEN = (USERNAME + ":" + PASSWORD).getBytes();
    private static final String AUTH_TOKEN = "Basic " + Base64.encodeBase64String(TOKEN);

}
