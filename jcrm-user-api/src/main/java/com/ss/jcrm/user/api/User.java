package com.ss.jcrm.user.api;

import com.ss.jcrm.dao.NamedEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface User extends NamedEntity {

    @NotNull byte[] getPassword();

    @NotNull byte[] getSalt();

    @Nullable Organization getOrganization();

    @NotNull Set<UserRole> getRoles();

    void setRoles(@NotNull Set<UserRole> roles);

    int getVersion();

    void setVersion(int version);
}
