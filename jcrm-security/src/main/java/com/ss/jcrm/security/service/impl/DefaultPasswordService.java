package com.ss.jcrm.security.service.impl;

import com.ss.jcrm.security.exception.InvalidKeySpecSecurityException;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.rlib.common.util.Utils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class DefaultPasswordService implements PasswordService {

    private static final ThreadLocal<Random> LOCAL_RANDOM = ThreadLocal.withInitial(SecureRandom::new);
    private static final ThreadLocal<SecretKeyFactory> LOCAL_SECRET_KEY_FACTORY =
        ThreadLocal.withInitial(() -> Utils.uncheckedGet("PBKDF2WithHmacSHA1", SecretKeyFactory::getInstance));

    private final int keyIterations;
    private final int keyLength;

    @Override
    public @NotNull byte[] getNextSalt() {
        var salt = new byte[16];
        var random = LOCAL_RANDOM.get();
        random.nextBytes(salt);
        return salt;
    }

    @Override
    public @NotNull byte[] hash(@NotNull char[] password, @NotNull byte[] salt) {

        var keyFactory = LOCAL_SECRET_KEY_FACTORY.get();
        var keySpec = new PBEKeySpec(password, salt, keyIterations, keyLength);

        Arrays.fill(password, Character.MIN_VALUE);
        try {
            return keyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpecSecurityException(e);
        } finally {
            keySpec.clearPassword();
        }
    }

    @Override
    public boolean isCorrect(
        @NotNull char[] password,
        @NotNull byte[] salt,
        @NotNull byte[] hash
    ) {
        var pwdHash = hash(password, salt);
        Arrays.fill(password, Character.MIN_VALUE);
        return Arrays.equals(pwdHash, hash);
    }

    @Override
    public @NotNull String nextPassword(int length) {

        var random = ThreadLocalRandom.current();
        var builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            var c = random.nextInt(62);
            if (c <= 9) {
                builder.append(String.valueOf(c));
            } else if (c < 36) {
                builder.append((char) ('a' + c - 10));
            } else {
                builder.append((char) ('A' + c - 36));
            }
        }

        return builder.toString();
    }

    @Override
    public @NotNull char[] nextCharPassword(int length) {
        return nextPassword(length).
            toCharArray();
    }

    @Override
    public @NotNull byte[] nextBytePassword(int length) {
        return nextPassword(length)
            .getBytes(StandardCharsets.UTF_8);
    }
}
