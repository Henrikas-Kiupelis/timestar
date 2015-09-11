package com.superum.api.v2.account;

import com.superum.api.v2.teacher.FullTeacherDTO;
import com.superum.helper.PartitionAccount;
import org.springframework.stereotype.Service;

@Service
public interface ValidAccountService {

    /**
     * Creates an account for a teacher
     */
    void createAccount(FullTeacherDTO fullTeacherDTO, PartitionAccount account);

    /**
     * Deletes an account for a teacher
     */
    void deleteAccount(FullTeacherDTO deletedTeacher, PartitionAccount account);

}
