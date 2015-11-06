package com.superum.api.v3.account.impl;

import com.google.common.primitives.Chars;
import com.superum.api.v3.account.PasswordGenerator;
import eu.goodlike.random.Random;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class PasswordGeneratorImpl implements PasswordGenerator {

    @Override
    public CompletableFuture<String> generate() {
        return CompletableFuture.supplyAsync(Random::getSecure, passwordGenThreadPool)
                .thenApply(rng -> rng.password(true, true, true, PASSWORD_LENGTH))
                .thenApply(pwd -> Chars.join("", pwd));
    }

    // PRIVATE

    private static final int PASSWORD_LENGTH = 7;

    private static final Executor passwordGenThreadPool = Executors.newCachedThreadPool();

}
