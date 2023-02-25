package crm.security.web.service;

import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public interface UnsafeTokenService extends TokenService {

  @NotNull String generateNewToken(
      long userId,
      @NotNull ZonedDateTime expiration,
      @NotNull ZonedDateTime notBefore,
      int refreshes,
      int passwordVersion);
}
