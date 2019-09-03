package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.web.resources.RestResource;
import com.ss.rlib.common.util.array.Array;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class UsersOutResource implements RestResource {

    private final UserOutResource[] users;

    public UsersOutResource(@NotNull Array<User> users) {
        this.users = users.stream()
            .map(UserOutResource::new)
            .toArray(UserOutResource[]::new);
    }
}
