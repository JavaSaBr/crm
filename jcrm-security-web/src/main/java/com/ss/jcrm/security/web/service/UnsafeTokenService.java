package com.ss.jcrm.security.web.service;

import com.ss.jcrm.user.api.User;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public interface UnsafeTokenService extends TokenService {

    @NotNull String generateNewToken(@NotNull User user, @NotNull ZonedDateTime expiry);
}
