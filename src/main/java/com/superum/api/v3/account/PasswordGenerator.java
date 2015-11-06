package com.superum.api.v3.account;

import java.util.concurrent.CompletableFuture;

public interface PasswordGenerator {

    CompletableFuture<String> generate();

}
