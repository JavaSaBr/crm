package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class UserOutResource extends MinimalUserOutResource {

    private final long created;
    private final long modified;

    public UserOutResource(@NotNull User user) {
        super(user);
        this.created = user.getCreated().toEpochMilli();
        this.modified = user.getModified().toEpochMilli();
    }
}
