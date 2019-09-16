package com.ss.jcrm.user.api;

import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.security.AccessRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface UserGroup extends NamedUniqEntity, VersionedUniqEntity {

    @NotNull Organization getOrganization();

    @NotNull Set<AccessRole> getRoles();

    void setRoles(@NotNull Set<AccessRole> roles);
}
