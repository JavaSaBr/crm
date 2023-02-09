package com.ss.jcrm.registration.web.resources;

import static crm.base.util.CommonUtils.toLongIds;
import crm.user.api.UserGroup;
import com.ss.jcrm.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;

public record UserGroupOutResource(
    @NotNull String name,
    long @NotNull [] roles,
    long id,
    long created,
    long modified
) implements RestResource {

    public static @NotNull UserGroupOutResource from(@NotNull UserGroup userGroup) {
        return new UserGroupOutResource(
            userGroup.name(),
            toLongIds(userGroup.roles()),
            userGroup.id(),
            userGroup.created().toEpochMilli(),
            userGroup.modified().toEpochMilli()
        );
    }
}
