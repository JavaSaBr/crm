package crm.security.service;

import crm.security.exception.InvalidKeySpecSecurityException;
import org.jetbrains.annotations.NotNull;

public interface PasswordService {

  byte @NotNull [] getNextSalt();

  /**
   * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
   */
  default byte @NotNull [] hash(@NotNull String password, byte @NotNull [] salt) {
    return hash(password.toCharArray(), salt);
  }

  /**
   * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
   */
  byte @NotNull [] hash(char @NotNull [] password, byte @NotNull [] salt);

  /**
   * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
   */
  default boolean isCorrect(@NotNull String password, byte @NotNull [] salt, byte @NotNull [] hash) {
    return isCorrect(password.toCharArray(), salt, hash);
  }

  /**
   * @throws InvalidKeySpecSecurityException if a secret key can't be generated.
   */
  boolean isCorrect(char @NotNull [] password, byte @NotNull [] salt, byte @NotNull [] hash);

  @NotNull String nextPassword(int length);

  char @NotNull [] nextCharPassword(int length);

  byte @NotNull [] nextBytePassword(int length);
}
