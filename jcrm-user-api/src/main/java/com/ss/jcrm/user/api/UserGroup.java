package com.ss.jcrm.user.api;

import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dao.VersionedEntity;
import com.ss.jcrm.security.AccessRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface UserGroup extends NamedEntity, VersionedEntity {

    @NotNull Organization getOrganization();

    @NotNull Set<AccessRole> getRoles();

    void setRoles(@NotNull Set<AccessRole> roles);
}
