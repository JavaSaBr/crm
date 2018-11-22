package com.ss.jcrm.security.web.token.impl;

import static java.time.ZonedDateTime.now;
import com.ss.jcrm.security.web.token.TokenService;
import com.ss.jcrm.security.web.token.exception.InvalidTokenException;
import com.ss.jcrm.security.web.token.exception.TokenExpiredException;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.UserDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class JjwtTokenService implements TokenService {

    // TODO need to load from property file
    private static final byte[] SECRET_ENCODED_KEY = new byte[]{
        126, -97, -127, -34, 88, -116, 9, -19, -45, -14, -35, 94, 58, 3, -105, 52, 75, 101, -101, 26, -91, 64, 36,
        -49, -34, 31, -94, 82, 127, -2, 87, -127, -116, 5, -101, 42, 29, -8, -101, 82, 66, 126, 6, -90, 7, 20, 9, 54,
        9, -6, -112, -121, 65, 77, 66, -57, 121, -25, 24, 34, -2, -61, 48, 121
    };

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_ENCODED_KEY);

    private final UserDao userDao;
    private final int expirationTime;

    @Override
    public @NotNull String generateNewToken(@NotNull User user) {
        return generateNewToken(user, now().plusMinutes(expirationTime));
    }

    protected @NotNull String generateNewToken(@NotNull User user, @NotNull ZonedDateTime expiry) {
        return Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .setExpiration(Date.from(expiry.toInstant()))
            .signWith(SECRET_KEY)
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
                .setSigningKey(SECRET_KEY)
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
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();

        return claims.getExpiration()
            .getTime() < System.currentTimeMillis();
    }
}
