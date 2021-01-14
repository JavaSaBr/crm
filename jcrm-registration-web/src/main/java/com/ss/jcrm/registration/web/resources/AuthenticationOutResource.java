package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.web.resources.RestResource;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@RequiredArgsConstructor
public class AuthenticationOutResource implements RestResource {

    @NotNull UserOutResource user;
    @NotNull String token;

    public AuthenticationOutResource(@NotNull User user, @NotNull String token) {
        this(new UserOutResource(user), token);
    }

    public AuthenticationOutResource(@NotNull String token, @NotNull User user) {
        this(new UserOutResource(user), token);
    }
}
