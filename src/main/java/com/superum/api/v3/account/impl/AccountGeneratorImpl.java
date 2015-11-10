package com.superum.api.v3.account.impl;

import com.superum.api.v1.account.AccountType;
import com.superum.api.v3.account.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
public class AccountGeneratorImpl implements AccountGenerator {

    @Override
    public CompletableFuture<Account> generateForTeacher(int id, String username) {
        CompletableFuture<String> password = passwordGenerator.generate();
        AccountType accountType = AccountType.TEACHER;
        Instant now = Instant.now();
        return password.thenApply(pwd -> Account.valueOf(id, username, accountType, pwd, now, now,
                accountRepository, accountEmailSender, passwordEncoder));
    }

    // CONSTRUCTORS

    @Autowired
    public AccountGeneratorImpl(PasswordGenerator passwordGenerator, AccountRepository accountRepository,
                                AccountEmailSender accountEmailSender, PasswordEncoder passwordEncoder) {
        this.passwordGenerator = passwordGenerator;
        this.accountRepository = accountRepository;
        this.accountEmailSender = accountEmailSender;
        this.passwordEncoder = passwordEncoder;
    }

    // PRIVATE

    private final PasswordGenerator passwordGenerator;
    private final AccountRepository accountRepository;
    private final AccountEmailSender accountEmailSender;
    private final PasswordEncoder passwordEncoder;

}
