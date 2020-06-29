package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class UserOutResource extends MinimalUserOutResource {

    long created;
    long modified;

    public UserOutResource(@NotNull User user) {
        super(user);
        this.created = user.getCreated().toEpochMilli();
        this.modified = user.getModified().toEpochMilli();
    }
}
