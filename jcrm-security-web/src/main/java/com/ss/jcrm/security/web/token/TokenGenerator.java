package com.ss.jcrm.security.web.token;

import com.ss.jcrm.user.api.User;
import org.jetbrains.annotations.NotNull;

public interface TokenGenerator {

    @NotNull String generateNewToken(@NotNull User user);
}
