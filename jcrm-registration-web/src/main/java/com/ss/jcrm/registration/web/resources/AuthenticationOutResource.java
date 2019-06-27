package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationOutResource implements RestResource {

    private UserOutResource user;
    private String token;

    public AuthenticationOutResource(@NotNull User user, @NotNull String token) {
        this(new UserOutResource(user), token);
    }

    public AuthenticationOutResource(@NotNull String token, @NotNull User user) {
        this(new UserOutResource(user), token);
    }
}
