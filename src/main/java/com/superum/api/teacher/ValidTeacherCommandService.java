package com.superum.api.teacher;

import com.superum.api.exception.InvalidRequestException;
import com.superum.exception.DatabaseException;
import com.superum.helper.PartitionAccount;
import org.springframework.stereotype.Service;

@Service
public interface ValidTeacherCommandService {

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
    FullTeacherDTO create(FullTeacherDTO fullTeacherDTO, PartitionAccount account);

    /**
     * <pre>
     * Updates an existing teacher
     *
     * The id fields must be set, and at least one more field must be set
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if teacher is null
     * @throws InvalidTeacherException if id field was not set, or no other fields were set
     * @throws TeacherNotFoundException if a teacher with specified id does not exist
     * @throws DatabaseException if database error occurred
     */
    void update(FullTeacherDTO fullTeacherDTO, int partitionId);

    /**
     * <pre>
     * Deletes a teacher with specified id
     *
     * The teacher's account is also deleted
     *
     * partitionId separates different app partitions (please refer to the API file or PartitionController)
     * </pre>
     *
     * @throws InvalidRequestException if id is illegal (<=0)
     * @throws TeacherNotFoundException if no teacher with this id exists
     * @throws DatabaseException if database error occurred
     */
    void delete(int teacherId, PartitionAccount account);

}
