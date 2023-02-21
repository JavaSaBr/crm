package crm.security.service.impl;

import crm.security.exception.InvalidKeySpecSecurityException;
import crm.security.service.PasswordService;
import com.ss.rlib.common.util.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultPasswordService implements PasswordService {

  private static final ThreadLocal<Random> LOCAL_RANDOM = ThreadLocal.withInitial(SecureRandom::new);
  private static final ThreadLocal<SecretKeyFactory> LOCAL_SECRET_KEY_FACTORY =
      ThreadLocal.withInitial(() -> Utils.uncheckedGet("PBKDF2WithHmacSHA1",
      SecretKeyFactory::getInstance));

  int keyIterations;
  int keyLength;

  @Override
  public byte @NotNull [] getNextSalt() {
    var salt = new byte[16];
    LOCAL_RANDOM.get().nextBytes(salt);
    return salt;
  }

  @Override
  public byte @NotNull [] hash(char @NotNull [] password, byte @NotNull [] salt) {

    var keyFactory = LOCAL_SECRET_KEY_FACTORY.get();
    var keySpec = new PBEKeySpec(password, salt, keyIterations, keyLength);

    Arrays.fill(password, Character.MIN_VALUE);
    try {
      return keyFactory
          .generateSecret(keySpec)
          .getEncoded();
    } catch (InvalidKeySpecException e) {
      throw new InvalidKeySpecSecurityException(e);
    } finally {
      keySpec.clearPassword();
    }
  }

  @Override
  public boolean isCorrect(char @NotNull [] password, byte @NotNull [] salt, byte @NotNull [] hash) {
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
        builder.append(c);
      } else if (c < 36) {
        builder.append((char) ('a' + c - 10));
      } else {
        builder.append((char) ('A' + c - 36));
      }
    }

    return builder.toString();
  }

  @Override
  public char @NotNull [] nextCharPassword(int length) {
    return nextPassword(length).toCharArray();
  }

  @Override
  public byte @NotNull [] nextBytePassword(int length) {
    return nextPassword(length).getBytes(StandardCharsets.UTF_8);
  }
}
