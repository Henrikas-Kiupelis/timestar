package com.superum.api.v2.account;

import com.google.common.primitives.Chars;
import com.superum.api.v1.account.Account;
import com.superum.api.v1.account.AccountDAO;
import com.superum.api.v1.account.AccountType;
import com.superum.api.v1.partition.PartitionService;
import com.superum.api.v2.teacher.FullTeacherDTO;
import com.superum.api.v2.teacher.ValidTeacherCommandService;
import com.superum.helper.PartitionAccount;
import eu.goodlike.libraries.spring.gmail.GMail;
import eu.goodlike.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ValidAccountServiceImpl implements ValidAccountService {

    @Override
    public void createAccount(FullTeacherDTO fullTeacherDTO, PartitionAccount account) {
        String name = partitionService.findPartition(account.partitionId()).getName();

        char[] randomPassword = Random.getDefault().password(true, true, false, PASSWORD_LENGTH);
        StringBuilder message = new StringBuilder()
                .append(EMAIL_BODY);
        for (char ch : randomPassword)
            message.append(ch);
        LOG.debug("Random password generated");

        String fullTitle = EMAIL_TITLE + name;
        mail.send(fullTeacherDTO.getEmail(), fullTitle, message.toString());
        LOG.debug("Sent email to teacher '{}': title - '{}'; body - '{}'", fullTeacherDTO, fullTitle, EMAIL_BODY + "[PROTECTED}");

        String securePassword = encoder.encode(Chars.join("", randomPassword));
        Arrays.fill(randomPassword, '?');
        LOG.debug("Password encoded and erased from memory");

        String accountName = account.usernameFor(fullTeacherDTO.getEmail());
        Account teacherAccount = accountDAO.create(new Account(fullTeacherDTO.getId(), accountName,
                AccountType.TEACHER.name(), securePassword.toCharArray()));
        LOG.debug("New Teacher Account created: {}", teacherAccount);
    }

    @Override
    public void deleteAccount(FullTeacherDTO deletedTeacher, PartitionAccount account) {
        accountDAO.delete(account.usernameFor(deletedTeacher.getEmail()));
    }

    // CONSTRUCTORS

    @Autowired
    public ValidAccountServiceImpl(PasswordEncoder encoder, GMail mail, AccountDAO accountDAO, PartitionService partitionService) {
        this.encoder = encoder;
        this.mail = mail;
        this.accountDAO = accountDAO;
        this.partitionService = partitionService;
    }

    // PRIVATE

    private final PasswordEncoder encoder;
    private final GMail mail;
    private final AccountDAO accountDAO;
    private final PartitionService partitionService;

    private static final int PASSWORD_LENGTH = 7;
    private static final String EMAIL_TITLE = "Your password for ";
    private static final String EMAIL_BODY = "Password: ";
    private static final Logger LOG = LoggerFactory.getLogger(ValidTeacherCommandService.class);

}
