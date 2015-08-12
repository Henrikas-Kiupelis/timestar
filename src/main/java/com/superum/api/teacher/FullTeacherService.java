package com.superum.api.teacher;

import com.superum.api.exception.InvalidRequestException;
import com.superum.exception.DatabaseException;
import com.superum.helper.PartitionAccount;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Responsible for handling the app logic in regard to teachers
 */
@Service
public interface FullTeacherService {

    /**
     * <pre>
     * Creates a new teacher
     *
     * The id fields must not be set, but all the other mandatory fields must be set
     *
     * After the teacher is created, an account is created for him (using the partitionId and email address),
     * and an email, containing his credentials is sent, both asynchronously to avoid delays
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return the created teacher with id field now set
     *
     * @throws InvalidRequestException if teacher is null
     * @throws InvalidTeacherException if id field was set or a mandatory field was not set
     * @throws DatabaseException if database error occurred
     */
    FullTeacher createTeacher(FullTeacher teacher, PartitionAccount account);

    /**
     * <pre>
     * Reads a teacher with specified id
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return teacher that was read
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DatabaseException if database error occurred
     */
    FullTeacher readTeacher(int teacherId, int partitionId);

    /**
     * <pre>
     * Updates an existing teacher
     *
     * The id fields must be set, and at least one more field must be set
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return the teacher data before update, as if read(teacherId) was called before updating
     *
     * @throws InvalidRequestException if teacher is null
     * @throws InvalidTeacherException if id field was not set, or no other fields were set
     * @throws TeacherNotFoundException if a teacher with specified id does not exist
     * @throws DatabaseException if database error occurred
     */
    FullTeacher updateTeacher(FullTeacher teacher, int partitionId);

    /**
     * <pre>
     * Deletes a teacher with specified id
     *
     * The teacher's account is also deleted
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return the deleted teacher data, as if read(teacherId) was called before deleting
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DatabaseException if database error occurred
     */
    FullTeacher deleteTeacher(int teacherId, PartitionAccount account);

    /**
     * <pre>
     * Reads all teachers; reading is paged;
     *
     * Pages start at 0, whereas the maximum amount is 100
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return teachers that were read
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws InvalidRequestException if page is illegal (<0)
     * @throws InvalidRequestException if amount is illegal (<=0 || >100)
     * @throws DatabaseException if database error occurred
     */
    List<FullTeacher> readTeachersAll(int page, int amount, int partitionId);

    /**
     * <pre>
     * Counts the total amount of teachers right now;
     *
     * This number MUST NOT be used to reason about teacherId - it is entirely possible some ids are missing, i.e.
     * due to deletion.
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return total count of teachers
     *
     * @throws DatabaseException if database error occurred
     */
    int countAll(int partitionId);

    /**
     * <pre>
     * Checks if a teacher exists
     *
     * The check is made by using all the set fields
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     * @return teacher, if it exists
     *
     * @throws InvalidRequestException if teacher is null
     * @throws InvalidTeacherException if teacher has no fields set (ignoring languages)
     * @throws TeacherNotFoundException if given teacher cannot be found
     * @throws DatabaseException if database error occurred
     */
    FullTeacher exists(FullTeacher teacher, int partitionId);

}
