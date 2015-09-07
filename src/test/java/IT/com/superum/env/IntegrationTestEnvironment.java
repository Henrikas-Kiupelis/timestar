package IT.com.superum.env;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.ResultVariation;
import com.superum.TimeStarBackEndApplication;
import com.superum.config.PersistenceContext;
import com.superum.config.SecurityConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jooq.lambda.Seq;
import org.jooq.lambda.Unchecked;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
import java.util.stream.Stream;

import static IT.com.superum.utils.JsonUtils.APPLICATION_JSON_UTF8;
import static IT.com.superum.utils.JsonUtils.convertObjectToJsonBytes;
import static IT.com.superum.utils.MockMvcUtils.fromResponse;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TimeStarBackEndApplication.class, PersistenceContext.class, SecurityConfig.class, HelperConfiguration.class, MockConfig.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public abstract class IntegrationTestEnvironment {

    public static final int TEST_PARTITION = 0;

    @Before
    public void setup() throws NamingException {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain)
                .build();
        db.init();
    }

    /*
    Should not be needed anymore due to MockConfig
    @After
    @Rollback(false)
    public void clean() {
        db.clean();
    }
    */

    // PROTECTED

    @Autowired
    protected DB db;

    protected MockHttpServletRequestBuilder get(String path) {
        return MockMvcRequestBuilders.get(path)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", TOKEN_HEADER);
    }

    protected MockHttpServletRequestBuilder post(String path, Object body) throws IOException {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(path)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", TOKEN_HEADER);
        return body == null ? builder : builder.content(convertObjectToJsonBytes(body));
    }

    protected MockHttpServletRequestBuilder put(String path, Object body) throws IOException {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(path)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", TOKEN_HEADER);
        return body == null ? builder : builder.content(convertObjectToJsonBytes(body));
    }

    protected MockHttpServletRequestBuilder delete(String path) {
        return MockMvcRequestBuilders.delete(path)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", TOKEN_HEADER);
    }

    protected Optional<MvcResult> performRequest(Function<String, MockHttpServletRequestBuilder> requestType, String path,
                                                 ResultVariation resultVariation, ResultMatcher... expect) throws Exception {
        ResultActions resultActions = mockMvc.perform(requestType.apply(path)).andDo(print());
        return handleExpects(resultActions, resultVariation, expect);
    }

    protected Optional<MvcResult> performRequest(BiFunction<String, Object, MockHttpServletRequestBuilder> requestType, String path, Object body,
                                                 ResultVariation resultVariation, ResultMatcher... expect) throws Exception {
        ResultActions resultActions = mockMvc.perform(requestType.apply(path, body)).andDo(print());
        return handleExpects(resultActions, resultVariation, expect);
    }

    protected Optional<MvcResult> performGet(String path, ResultVariation resultVariation,
                                             ResultMatcher... expect) throws Exception {
        return performRequest(Unchecked.function(this::get), path, resultVariation, expect);
    }

    protected Optional<MvcResult> performPost(String path, Object body, ResultVariation resultVariation,
                                             ResultMatcher... expect) throws Exception {
        return performRequest(Unchecked.biFunction(this::post), path, body, resultVariation, expect);
    }

    protected Optional<MvcResult> performPut(String path, Object body, ResultVariation resultVariation,
                                             ResultMatcher... expect) throws Exception {
        return performRequest(Unchecked.biFunction(this::put), path, body, resultVariation, expect);
    }

    protected Optional<MvcResult> performDelete(String path, ResultVariation resultVariation,
                                             ResultMatcher... expect) throws Exception {
        return performRequest(Unchecked.function(this::delete), path, resultVariation, expect);
    }

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

    protected int readCount(MvcResult result) throws IOException {
        return fromResponse(result, Integer.class);
    }

    protected static final int OLD_TEACHER_ID = 1;
    protected static final int OLD_CUSTOMER_ID = 1;
    protected static final int OLD_GROUP_ID = 1;
    protected static final int OLD_STUDENT_ID = 1;
    protected static final long OLD_LESSON_ID = 1;

    protected static final int NEW_TEACHER_ID = 3;
    protected static final int NEW_CUSTOMER_ID = 31;
    protected static final int NEW_GROUP_ID = 3;
    protected static final int NEW_STUDENT_ID = 3;
    protected static final long NEW_LESSON_ID = 3;

    // PRIVATE

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    private Optional<MvcResult> handleExpects(ResultActions resultActions, ResultVariation resultVariation, ResultMatcher... expect) throws Exception {
        if (resultVariation != ResultVariation.BAD)
            return Optional.of(expect(okActions(resultActions, resultVariation), expect).andReturn());

        expect(resultActions, expect);
        return Optional.empty();
    }

    private ResultActions expect(ResultActions seed, ResultMatcher... expect) {
        return Seq.seq(Stream.of(expect)).foldLeft(seed, Unchecked.biFunction(ResultActions::andExpect));
    }

    private ResultActions okActions(ResultActions seed, ResultVariation resultVariation) throws Exception {
        ResultActions resultActions = seed.andExpect(status().isOk());
        return resultVariation == ResultVariation.OK_NO_BODY ? resultActions : resultActions.andExpect(
                content().contentType(APPLICATION_JSON_UTF8));
    }

    private static final String USERNAME = TEST_PARTITION + ".test";
    private static final String PASSWORD = "test";
    private static final byte[] TOKEN = (USERNAME + ":" + PASSWORD).getBytes();
    private static final String TOKEN_HEADER = "Basic " + Base64.encodeBase64String(TOKEN);

}
