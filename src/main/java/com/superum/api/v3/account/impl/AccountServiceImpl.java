package com.superum.api.v3.account.impl;

import com.superum.api.v1.partition.Partition;
import com.superum.api.v1.partition.PartitionService;
import com.superum.api.v3.account.Account;
import com.superum.api.v3.account.AccountGenerator;
import com.superum.api.v3.account.AccountService;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Override
    public void registerTeacher(int id, String email) {
        PartitionAccount partitionAccount = new PartitionAccount();
        String username = partitionAccount.usernameFor(email);
        CompletableFuture<Account> futureAccount = accountGenerator.generateForTeacher(id, username)
                .thenApplyAsync(Account::create, accountCreatorPool);
        CompletableFuture.completedFuture(partitionAccount.partitionId())
                .thenApplyAsync(partitionService::findPartition, accountCreatorPool)
                .thenApply(Partition::getName)
                .thenAcceptBoth(futureAccount, (name, account) -> account.sendCredentials(email, name));
    }

    // CONSTRUCTORS

    @Autowired
    public AccountServiceImpl(AccountGenerator accountGenerator, PartitionService partitionService) {
        this.accountGenerator = accountGenerator;
        this.partitionService = partitionService;
    }

    // PRIVATE

    private final AccountGenerator accountGenerator;
    private final PartitionService partitionService;

    private static final Executor accountCreatorPool = Executors.newCachedThreadPool();

}
