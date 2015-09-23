package com.superum.api.v3.lesson;

import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.api.v2.lesson.LessonNotFoundException;
import com.superum.api.v2.student.StudentNotFoundException;
import com.superum.api.v2.teacher.TeacherNotFoundException;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * <pre>
 * Defines queries which allow to retrieve the state of the system in regards to lessons, i.e. create a new lesson
 *
 * The methods should not make any changes to the system under any circumstances
 * </pre>
 */
public interface LessonQueries {

    /**
     * @return lesson with id of lessonId
     * @throws LessonNotFoundException if no lesson with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    FetchedLesson readById(long lessonId);

    /**
     * @return lessons between start and end, starting at (page * amount); the amount of them is returned
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<FetchedLesson> readAll(int page, int amount, long start, long end);

    /**
     * Every lesson is for a certain group, which is its groupId
     * @return lessons with given groupId between start and end, starting at (page * amount);
     * the amount of them is returned
     * @throws GroupNotFoundException if no group with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<FetchedLesson> readForGroup(int groupId, int page, int amount, long start, long end);

    /**
     * Every lesson is done by a certain teacher, which is its teacherId
     * @return lessons with given teacherId between start and end, starting at (page * amount);
     * the amount of them is returned
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<FetchedLesson> readForTeacher(int teacherId, int page, int amount, long start, long end);

    /**
     * Every lesson has a group, and members of this group can belong to customers, which are its customerIds
     * @return lessons with given customerId between start and end, starting at (page * amount);
     * the amount of them is returned
     * @throws CustomerNotFoundException if no customer with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<FetchedLesson> readForCustomer(int customerId, int page, int amount, long start, long end);

    /**
     * Every lesson can be attended by students, which are its studentIds
     * @return lessons with given studentId between start and end, starting at (page * amount);
     * the amount of them is returned
     * @throws StudentNotFoundException if no student with this id exists
     * @throws DataAccessException if an unexpected database error occurred
     */
    List<FetchedLesson> readForStudent(int studentId, int page, int amount, long start, long end);

}
