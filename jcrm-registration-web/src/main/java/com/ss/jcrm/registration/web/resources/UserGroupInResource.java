package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.web.resources.RestResource;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
public class UserGroupInResource implements RestResource {

    public static @NotNull UserGroupInResource newResource(
        @Nullable String name,
        @Nullable int[] roles,
        @Nullable long[] users
    ) {
        return new UserGroupInResource(
            name,
            roles,
            users,
            0
        );
    }

    @Nullable String name;
    @Nullable int[] roles;
    @Nullable long[] users;

    long id;

    @JsonCreator
    public UserGroupInResource(
        @JsonProperty("name") @Nullable String name,
        @JsonProperty("roles") @Nullable int[] roles,
        @JsonProperty("users") @Nullable long[] users,
        @JsonProperty("id") long id
    ) {
        this.name = name;
        this.users = users;
        this.roles = roles;
        this.id = id;
    }
}
