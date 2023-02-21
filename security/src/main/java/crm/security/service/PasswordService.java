package crm.security.service;

import crm.security.exception.InvalidKeySpecSecurityException;
import org.jetbrains.annotations.NotNull;

public interface PasswordService {

    @NotNull byte[] getNextSalt();

    /**
     * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
     */
    default @NotNull byte[] hash(@NotNull String password, @NotNull byte[] salt) {
        return hash(password.toCharArray(), salt);
    }

    /**
     * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
     */
    @NotNull byte[] hash(@NotNull char[] password, @NotNull byte[] salt);

    /**
     * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
     */
    default boolean isCorrect(@NotNull String password, @NotNull byte[] salt, @NotNull byte[] hash) {
        return isCorrect(password.toCharArray(), salt, hash);
    }

    /**
     * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
     */
    boolean isCorrect(@NotNull char[] password, @NotNull byte[] salt, @NotNull byte[] hash);

    @NotNull String nextPassword(int length);

    @NotNull char[] nextCharPassword(int length);

    @NotNull byte[] nextBytePassword(int length);
}
