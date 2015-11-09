package com.superum.api.v3.account.impl;

import com.superum.api.v1.account.AccountType;
import com.superum.api.v1.partition.Partition;
import com.superum.api.v1.partition.PartitionService;
import com.superum.api.v3.account.Account;
import com.superum.api.v3.account.AccountGenerator;
import com.superum.api.v3.account.AccountRepository;
import com.superum.api.v3.account.AccountServiceExt;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class AccountServiceExtImpl implements AccountServiceExt {

    @Override
    public void registerTeacher(int id, String email) {
        PartitionAccount partitionAccount = new PartitionAccount();
        String username = partitionAccount.usernameFor(email);
        CompletableFuture<Account> futureAccount = accountGenerator.generateForTeacher(id, username)
                .thenApply(Account::create);
        CompletableFuture.completedFuture(partitionAccount.partitionId())
                .thenApplyAsync(partitionService::findPartition)
                .thenApply(Partition::getName)
                .thenAcceptBoth(futureAccount, (name, account) -> account.sendCredentials(email, name));
    }

    @Override
    public void updateTeacherEmail(String originalEmail, String newEmail) {
        PartitionAccount partitionAccount = new PartitionAccount();
        String originalUsername = partitionAccount.usernameFor(originalEmail);
        String newUsername = partitionAccount.usernameFor(newEmail);
        CompletableFuture.runAsync(() -> accountRepository.updateUsername(originalUsername, newUsername));
    }

    @Override
    public void deleteAccount(int id, AccountType accountType) {
        CompletableFuture.runAsync(() -> accountRepository.deleteAccount(id, accountType.name()));
    }

    // CONSTRUCTORS

    @Autowired
    public AccountServiceExtImpl(AccountGenerator accountGenerator, AccountRepository accountRepository, PartitionService partitionService) {
        this.accountGenerator = accountGenerator;
        this.accountRepository = accountRepository;
        this.partitionService = partitionService;
    }

    // PRIVATE

    private final AccountGenerator accountGenerator;
    private final AccountRepository accountRepository;
    private final PartitionService partitionService;

}
