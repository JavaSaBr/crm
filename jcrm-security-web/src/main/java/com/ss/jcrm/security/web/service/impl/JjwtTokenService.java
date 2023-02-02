package com.ss.jcrm.security.web.service.impl;

import static java.time.ZonedDateTime.now;
import com.ss.jcrm.security.web.exception.*;
import com.ss.jcrm.security.web.service.UnsafeTokenService;
import crm.user.api.User;
import crm.user.api.dao.UserDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;

@Log4j2
@AllArgsConstructor
public class JjwtTokenService implements UnsafeTokenService {

    private static final String TOKEN_REFRESHES_FIELD = "refreshes";
    private static final String TOKEN_PWD_VERSION_FIELD = "pwd_version";

    private final UserDao userDao;
    private final SecretKey secretKey;
    private final Random random;

    private final int expirationTime;
    private final int maxRefreshes;

    public JjwtTokenService(@NotNull UserDao userDao, @NotNull byte[] secretKey, int expirationTime, int maxRefreshes) {
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

        return builder.delete(length, builder.length())
            .toString();
    }

    @Override
    public @NotNull String generateNewToken(@NotNull User user) {
        var now = now();
        return generateNewToken(
            user.id(),
            now.plusMinutes(expirationTime),
            now,
            0,
            user.passwordVersion()
        );
    }

    @Override
    public @NotNull String generateNewToken(
        long userId,
        @NotNull ZonedDateTime expiration,
        @NotNull ZonedDateTime notBefore,
        int refreshes,
        int passwordVersion
    ) {
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setExpiration(Date.from(expiration.toInstant()))
            .setNotBefore(Date.from(notBefore.toInstant()))
            .claim(TOKEN_REFRESHES_FIELD, refreshes)
            .claim(TOKEN_PWD_VERSION_FIELD, passwordVersion)
            .signWith(secretKey)
            .compact();
    }

    @Override
    public @NotNull Mono<@NotNull User> findUser(@NotNull String token) {
        var claims = getPossibleExpiredClaims(token);
        return userDao.requireById(Long.parseLong(claims.getSubject()));
    }

    @Override
    public @NotNull Mono<@NotNull User> findUserIfNotExpired(@NotNull String token) {
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
            throw new ChangedPasswordTokenException(
                "The token [" + token + "] cannot be used for the user [" + user.email() +
                    "] because his password was changed.");
        }

        var now = now();

        return generateNewToken(user.id(),
            now.plusMinutes(expirationTime),
            now,
            refreshes + 1,
            user.passwordVersion()
        );
    }

    private @NotNull Claims getPossibleExpiredClaims(@NotNull String token) {
        try {

            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        } catch (ExpiredJwtException e) {

            var claims = e.getClaims();

            if (new Date().before(claims.getNotBefore())) {
                throw new PrematureTokenException(
                    "The token [" + token + "] is from future [" + claims.getNotBefore() + "].");
            }

            return claims;

        } catch (PrematureJwtException e) {
            throw new PrematureTokenException(
                "The token [" + token + "] is from future [" + e.getClaims().getNotBefore() + "].");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            throw new InvalidTokenException(e);
        }
    }

    private @NotNull Claims getClaims(@NotNull String token) {
        try {

            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(
                "The token [" + token + "] is expired [" + e.getClaims().getExpiration() + "].");
        } catch (PrematureJwtException e) {
            throw new PrematureTokenException(
                "The token [" + token + "] is from future [" + e.getClaims().getNotBefore() + "].");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            throw new InvalidTokenException(e);
        }
    }
}
