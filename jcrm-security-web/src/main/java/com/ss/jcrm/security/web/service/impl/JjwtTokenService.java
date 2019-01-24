package com.ss.jcrm.security.web.service.impl;

import static java.time.ZonedDateTime.now;
import com.ss.jcrm.security.web.exception.InvalidTokenException;
import com.ss.jcrm.security.web.exception.TokenExpiredException;
import com.ss.jcrm.security.web.service.UnsafeTokenService;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.rlib.common.util.StringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class JjwtTokenService implements UnsafeTokenService {

    private final UserDao userDao;
    private final SecretKey secretKey;
    private final int expirationTime;

    public JjwtTokenService(@NotNull UserDao userDao, @NotNull String secretKey, int expirationTime) {
        this.userDao = userDao;
        this.secretKey = Keys.hmacShaKeyFor(StringUtils.hexStringToBytes(secretKey));
        this.expirationTime = expirationTime;
    }

    @Override
    public @NotNull String generateNewToken(@NotNull User user) {
        return generateNewToken(user, now().plusMinutes(expirationTime));
    }

    @Override
    public @NotNull String generateNewToken(@NotNull User user, @NotNull ZonedDateTime expiry) {
        return Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .setExpiration(Date.from(expiry.toInstant()))
            .signWith(secretKey)
            .compact();
    }

    @Override
    public @NotNull User findUserIfNotExpired(@NotNull String token) {
        var claims = getClaims(token);
        return userDao.requireById(Long.parseLong(claims.getSubject()));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull User> findUserIfNotExpiredAsync(@NotNull String token) {
        var claims = getClaims(token);
        return userDao.requireByIdAsync(Long.parseLong(claims.getSubject()));
    }

    private @NotNull Claims getClaims(@NotNull String token) {

        Claims claims;
        try {

             claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("The token [" + token + "] is expired.");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            throw new InvalidTokenException(e);
        }

        return claims;
    }

    @Override
    public boolean isExpired(@NotNull String token) {

        var claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        return claims.getExpiration()
            .getTime() < System.currentTimeMillis();
    }
}
