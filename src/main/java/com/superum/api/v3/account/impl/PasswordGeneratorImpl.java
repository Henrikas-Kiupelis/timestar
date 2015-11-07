package com.superum.api.v3.account.impl;

import com.google.common.primitives.Chars;
import com.superum.api.v3.account.PasswordGenerator;
import eu.goodlike.random.Random;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PasswordGeneratorImpl implements PasswordGenerator {

    @Override
    public CompletableFuture<String> generate() {
        return CompletableFuture.supplyAsync(Random::getSecure)
                .thenApply(rng -> rng.password(true, true, true, PASSWORD_LENGTH))
                .thenApply(pwd -> Chars.join("", pwd));
    }

    // PRIVATE

    private static final int PASSWORD_LENGTH = 7;

}
