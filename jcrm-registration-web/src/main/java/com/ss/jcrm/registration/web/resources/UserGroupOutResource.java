package com.ss.jcrm.registration.web.resources;

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

    public UserGroupOutResource(@NotNull UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
        this.roles = userGroup.streamRoles()
            .mapToInt(value -> Math.toIntExact(value.getId()))
            .toArray();
    }
}
