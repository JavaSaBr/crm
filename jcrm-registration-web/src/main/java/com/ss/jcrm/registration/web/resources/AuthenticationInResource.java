package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.web.resources.RestResource;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

@Value
public class AuthenticationInResource implements RestResource {

    @Nullable String login;
    @Nullable char[] password;

    public AuthenticationInResource(
        @JsonProperty("login") @Nullable String login,
        @JsonProperty("password") @Nullable char[] password
    ) {
        this.login = login;
        this.password = password;
    }
}
