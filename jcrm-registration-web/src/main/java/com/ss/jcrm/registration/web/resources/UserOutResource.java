package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOutResource extends MinimalUserOutResource {

    int[] roles;

    long created;
    long modified;

    public UserOutResource(@NotNull User user) {
        super(user);
        this.created = user.getCreated().toEpochMilli();
        this.modified = user.getModified().toEpochMilli();
        this.roles = user.getRoles()
            .stream()
            .mapToInt(value -> Math.toIntExact(value.getId()))
            .toArray();
    }
}
