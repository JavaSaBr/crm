package com.ss.jcrm.registration.web.resources;

import static com.ss.jcrm.base.utils.CommonUtils.toLongIds;
import com.ss.jcrm.user.api.UserGroup;
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
            userGroup.getName(),
            toLongIds(userGroup.getRoles()),
            userGroup.getId(),
            userGroup.getCreated().toEpochMilli(),
            userGroup.getModified().toEpochMilli()
        );
    }
}
