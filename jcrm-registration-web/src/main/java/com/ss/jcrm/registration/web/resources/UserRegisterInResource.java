package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.web.resources.RestResource;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@Value
public class UserRegisterInResource implements RestResource {

    @Nullable String name;
    @Nullable String password;
    @Nullable long[] roles;

    public UserRegisterInResource(
        @JsonProperty("name") @Nullable String name,
        @JsonProperty("password") @Nullable String password,
        @JsonProperty("roles") @Nullable long[] roles
    ) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }
}
