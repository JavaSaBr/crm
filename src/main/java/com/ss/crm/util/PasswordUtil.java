package com.ss.crm.util;

import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

/**
 * @author JavaSaBr
 */
public class PasswordUtil {

    @NotNull
    private static final ThreadLocal<Random> THREAD_LOCAL_RANDOM = ThreadLocal.withInitial(SecureRandom::new);

    @NotNull
    private static final ThreadLocal<SecretKeyFactory> THREAD_LOCAL_SECRET_KEY_FACTORY = ThreadLocal.withInitial(() -> {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    });

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    /**
     * Returns a random salt to be used to hash a password.
     *
     * @return a 16 bytes random salt.
     */
    public static byte[] getNextSalt() {
        final byte[] salt = new byte[16];
        THREAD_LOCAL_RANDOM.get().nextBytes(salt);
        return salt;
    }

    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password the password to be hashed
     * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method
     * @return the hashed password with a pinch of salt
     */
    public static byte[] hash(final char[] password, final byte[] salt) {

        final PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);

        Arrays.fill(password, Character.MIN_VALUE);
        try {
            final SecretKeyFactory keyFactory = THREAD_LOCAL_SECRET_KEY_FACTORY.get();
            final SecretKey secretKey = keyFactory.generateSecret(spec);
            return secretKey.getEncoded();
        } catch (final InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Returns true if the given password and salt match the hashed value, false otherwise.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    public static boolean isExpectedPassword(final char[] password, final byte[] salt, final byte[] expectedHash) {
        byte[] pwdHash = hash(password, salt);
        Arrays.fill(password, Character.MIN_VALUE);
        return pwdHash.length == expectedHash.length && Arrays.equals(pwdHash, expectedHash);
    }
}
