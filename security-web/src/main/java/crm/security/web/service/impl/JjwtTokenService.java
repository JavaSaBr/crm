package crm.security.web.service.impl;

import static java.time.ZonedDateTime.now;
;
import crm.security.web.exception.ChangedPasswordTokenException;
import crm.security.web.exception.ExpiredTokenException;
import crm.security.web.exception.InvalidTokenException;
import crm.security.web.exception.PrematureTokenException;
import crm.security.web.service.UnsafeTokenService;
import crm.security.web.exception.MaxRefreshedTokenException;
import crm.user.api.User;
import crm.user.api.dao.UserDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JjwtTokenService implements UnsafeTokenService {

  private static final String TOKEN_REFRESHES_FIELD = "refreshes";
  private static final String TOKEN_PWD_VERSION_FIELD = "pwd_version";

  @NotNull UserDao userDao;
  @NotNull SecretKey secretKey;
  @NotNull Random random;

  int expirationTime;
  int maxRefreshes;

  public JjwtTokenService(@NotNull UserDao userDao, byte @NotNull [] secretKey, int expirationTime, int maxRefreshes) {
    this.userDao = userDao;
    this.secretKey = Keys.hmacShaKeyFor(secretKey);
    this.expirationTime = expirationTime;
    this.maxRefreshes = maxRefreshes;
    try {
      this.random = SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    log.info("Expiration time: {}", expirationTime);
    log.info("Max refreshes: {}", maxRefreshes);
  }

  @Override
  public @NotNull String generateActivateCode(int length) {

    if (length < 1) {
      throw new IllegalArgumentException("The length cannot be less than 1");
    }

    var buffer = ByteBuffer.allocate(length);

    random.nextBytes(buffer.array());

    buffer.position(length);
    buffer.flip();

    var builder = new StringBuilder(length);

    while (buffer.hasRemaining()) {
      builder.append(buffer.get() & 0xFF);
    }

    return builder
        .delete(length, builder.length())
        .toString();
  }

  @Override
  public @NotNull String generateNewToken(@NotNull User user) {
    var now = now();
    return generateNewToken(user.id(), now.plusMinutes(expirationTime), now, 0, user.passwordVersion());
  }

  @Override
  public @NotNull String generateNewToken(
      long userId,
      @NotNull ZonedDateTime expiration,
      @NotNull ZonedDateTime notBefore,
      int refreshes,
      int passwordVersion) {
    return Jwts
        .builder()
        .setSubject(String.valueOf(userId))
        .setExpiration(Date.from(expiration.toInstant()))
        .setNotBefore(Date.from(notBefore.toInstant()))
        .claim(TOKEN_REFRESHES_FIELD, refreshes)
        .claim(TOKEN_PWD_VERSION_FIELD, passwordVersion)
        .signWith(secretKey)
        .compact();
  }

  @Override
  public @NotNull Mono<User> findUser(@NotNull String token) {
    var claims = getPossibleExpiredClaims(token);
    return userDao.requireById(Long.parseLong(claims.getSubject()));
  }

  @Override
  public @NotNull Mono<User> findUserIfNotExpired(@NotNull String token) {
    var claims = getClaims(token);
    return userDao.requireById(Long.parseLong(claims.getSubject()));
  }

  @Override
  public @NotNull String refresh(@NotNull User user, @NotNull String token) {

    var claims = getPossibleExpiredClaims(token);
    var refreshes = (Integer) claims.get(TOKEN_REFRESHES_FIELD);

    if (refreshes == null) {
      throw new InvalidTokenException("The token [" + token + "] doesn't include field 'refreshes'.");
    } else if (refreshes > maxRefreshes) {
      throw new MaxRefreshedTokenException("The token [" + token + "] has reached max refreshes.");
    }

    var passwordVersion = (Integer) claims.get(TOKEN_PWD_VERSION_FIELD);

    if (passwordVersion == null) {
      throw new InvalidTokenException("The token [" + token + "] doesn't include field 'refreshes'.");
    } else if (passwordVersion != user.passwordVersion()) {
      throw new ChangedPasswordTokenException("The token [" + token + "] cannot be used for the user [" + user.email()
          + "] because his password was changed.");
    }

    var now = now();

    return generateNewToken(user.id(), now.plusMinutes(expirationTime), now, refreshes + 1, user.passwordVersion());
  }

  private @NotNull Claims getPossibleExpiredClaims(@NotNull String token) {
    try {

      return Jwts
          .parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(token)
          .getBody();

    } catch (ExpiredJwtException e) {

      var claims = e.getClaims();

      if (new Date().before(claims.getNotBefore())) {
        throw new PrematureTokenException("The token [" + token + "] is from future [" + claims.getNotBefore() + "].");
      }

      return claims;

    } catch (PrematureJwtException e) {
      throw new PrematureTokenException("The token [" + token + "] is from future [" + e
          .getClaims()
          .getNotBefore() + "].");
    } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
      throw new InvalidTokenException(e);
    }
  }

  private @NotNull Claims getClaims(@NotNull String token) {
    try {

      return Jwts
          .parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(token)
          .getBody();

    } catch (ExpiredJwtException e) {
      throw new ExpiredTokenException("The token [" + token + "] is expired [" + e
          .getClaims()
          .getExpiration() + "].");
    } catch (PrematureJwtException e) {
      throw new PrematureTokenException("The token [" + token + "] is from future [" + e
          .getClaims()
          .getNotBefore() + "].");
    } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
      throw new InvalidTokenException(e);
    }
  }
}
