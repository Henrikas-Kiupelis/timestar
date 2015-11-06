package com.superum.api.v3.account;

import java.util.concurrent.CompletableFuture;

public interface AccountGenerator {

    CompletableFuture<Account> generateForTeacher(int id, String username);

}
