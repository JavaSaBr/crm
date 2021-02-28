package com.ss.jcrm.user.api;

import com.ss.jcrm.dao.ChangeTrackedEntity;
import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.security.AccessRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public interface UserGroup extends ChangeTrackedEntity, NamedUniqEntity, VersionedUniqEntity {

    void setName(@NotNull String name);

    @NotNull Organization getOrganization();

    @NotNull Set<AccessRole> getRoles();

    default @NotNull Stream<AccessRole> streamRoles() {
        return getRoles().stream();
    }

    void setRoles(@NotNull Set<AccessRole> roles);
}
