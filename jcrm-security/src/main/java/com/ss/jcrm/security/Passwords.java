package com.ss.jcrm.security;

import com.ss.jcrm.security.exception.InvalidKeySpecSecurityException;
import com.ss.rlib.common.util.Utils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Passwords {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final ThreadLocal<Random> LOCAL_RANDOM = ThreadLocal.withInitial(SecureRandom::new);
    private static final ThreadLocal<SecretKeyFactory> LOCAL_SECRET_KEY_FACTORY =
        ThreadLocal.withInitial(() -> Utils.get("PBKDF2WithHmacSHA1", SecretKeyFactory::getInstance));

    public static @NotNull byte[] getNextSalt() {
        var salt = new byte[16];
        var random = LOCAL_RANDOM.get();
        random.nextBytes(salt);
        return salt;
    }

    public static @NotNull byte[] hash(@NotNull String password, @NotNull byte[] salt) {
        return hash(password.toCharArray(), salt);
    }

    public static @NotNull byte[] hash(@NotNull char[] password, @NotNull byte[] salt) {

        var keyFactory = LOCAL_SECRET_KEY_FACTORY.get();
        var keySpec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);

        Arrays.fill(password, Character.MIN_VALUE);
        try {
            return keyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpecSecurityException(e);
        } finally {
            keySpec.clearPassword();
        }
    }

    public static boolean isCorrect(
        @NotNull String password,
        @NotNull byte[] salt,
        @NotNull byte[] hash
    ) {
        return isCorrect(password.toCharArray(), salt, hash);
    }

    public static boolean isCorrect(
        @NotNull char[] password,
        @NotNull byte[] salt,
        @NotNull byte[] hash
    ) {

        var pwdHash = hash(password, salt);
        Arrays.fill(password, Character.MIN_VALUE);
        return Arrays.equals(pwdHash, hash);
    }

    public static @NotNull String nextPassword(int length) {

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

    public static @NotNull char[] nextCharPassword(int length) {
        return nextPassword(length).
            toCharArray();
    }

    public static @NotNull byte[] nextBytePassword(int length) {
        return nextPassword(length)
            .getBytes(UTF_8);
    }
}
