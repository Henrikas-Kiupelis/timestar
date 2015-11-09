package com.superum.api.v3.teacher;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.v3.teacher.dto.FetchedTeacher;
import com.superum.api.v3.teacher.dto.SuppliedTeacher;
import eu.goodlike.misc.CommonControllerLogic;
import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/timestar/api/v3/teacher")
public class TeacherController implements CommonControllerLogic {

    // COMMANDS

    @RequestMapping(method = POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FetchedTeacher create(@RequestBody SuppliedTeacher suppliedTeacher) {
        Null.check(suppliedTeacher).ifAny(() -> new InvalidRequestException("Teacher cannot be null"));
        LOG.info("Create teacher request: {}", suppliedTeacher);

        FetchedTeacher createdTeacher = teacherCommands.create(suppliedTeacher);
        LOG.info("Successfully created teacher: {}", createdTeacher);

        return createdTeacher;
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = PUT, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public void update(@PathVariable int id, @RequestBody SuppliedTeacher suppliedTeacher) {
        Null.check(suppliedTeacher).ifAny(() -> new InvalidRequestException("Teacher cannot be null"));
        LOG.info("Update teacher with id {} request: {}", id, suppliedTeacher);

        teacherCommands.update(suppliedTeacher, id);
        LOG.info("Successfully updated teacher");
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    @ResponseBody
    public void delete(@PathVariable int id) {
        validateId("Teacher", id, InvalidRequestException::new);
        LOG.info("Delete teacher with id {} request", id);

        teacherCommands.delete(id);
        LOG.info("Successfully deleted teacher");
    }

    // QUERIES

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FetchedTeacher read(@PathVariable int id) {
        validateId("Teacher", id, InvalidRequestException::new);
        LOG.info("Read teacher with id {} request", id);

        FetchedTeacher teacher = teacherQueries.readById(id);
        LOG.info("Teacher retrieved: {}", teacher);

        return teacher;
    }



    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<FetchedTeacher> readAll(@RequestParam(value="page", required=false) Integer page,
                                        @RequestParam(value="per_page", required=false) Integer per_page) {
        page = validatePage(page, InvalidRequestException::new);
        per_page = validatePerPage(per_page, InvalidRequestException::new);
        LOG.info("Read all teachers request; page: {}, per_page: {}", page, per_page);

        List<FetchedTeacher> teachers = teacherQueries.readAll(page, per_page);
        LOG.info("Teachers retrieved: {}", teachers);

        return teachers;
    }

    @RequestMapping(value = "/count", method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public int countAll() {
        LOG.info("Count all teachers request");

        int count = teacherQueries.countAll();
        LOG.info("Amount of teachers: {}", count);

        return count;
    }

    // CONSTRUCTORS

    @Autowired
    public TeacherController(TeacherCommands teacherCommands, TeacherQueries teacherQueries) {
        this.teacherCommands = teacherCommands;
        this.teacherQueries = teacherQueries;
    }

    // PRIVATE

    private final TeacherCommands teacherCommands;
    private final TeacherQueries teacherQueries;

    private static final Logger LOG = LoggerFactory.getLogger(TeacherController.class);

}
