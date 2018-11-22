package com.ss.jcrm.security.web.token.impl;

import static java.time.ZonedDateTime.now;
import com.ss.jcrm.security.web.token.TokenGenerator;
import com.ss.jcrm.user.api.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

public class JjwtTokenGenerator implements TokenGenerator {

    @Override
    public @NotNull String generateNewToken(@NotNull User user) {

        final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        final ZonedDateTime expiry = now().plusHours(10);

        final String compactJws = Jwts.builder()
            .setSubject(user.getName())
            .setNotBefore(Date.from(expiry.toInstant()))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();


        return null;
    }
}
