package com.ss.jcrm.security.web.service;

import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public interface UnsafeTokenService extends TokenService {

    default @NotNull String generateNewToken(long userId, @NotNull ZonedDateTime expiration) {
        return generateNewToken(userId, expiration, ZonedDateTime.now(), 0);
    }

    @NotNull String generateNewToken(
        long userId,
        @NotNull ZonedDateTime expiration,
        @NotNull ZonedDateTime notBefore,
        int refreshes
    );
}
