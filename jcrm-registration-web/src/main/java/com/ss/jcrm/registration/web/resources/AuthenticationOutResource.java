package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;

public record AuthenticationOutResource(
    @NotNull String token,
    @NotNull UserOutResource user
) implements RestResource {

    public static @NotNull AuthenticationOutResource from(@NotNull String token, @NotNull User user) {
        return new AuthenticationOutResource(token, UserOutResource.from(user));
    }
}
