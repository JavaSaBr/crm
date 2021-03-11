package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.user.api.UserGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGroupOutResource {

    @NotNull String name;
    @NotNull int[] roles;

    long id;

    @JsonCreator
    public UserGroupOutResource(
        @JsonProperty("name") @NotNull String name,
        @JsonProperty("roles") @NotNull int[] roles,
        @JsonProperty("id") long id
    ) {
        this.name = name;
        this.roles = roles;
        this.id = id;
    }

    public UserGroupOutResource(@NotNull UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
        this.roles = userGroup.streamRoles()
            .mapToInt(value -> Math.toIntExact(value.getId()))
            .toArray();
    }
}
