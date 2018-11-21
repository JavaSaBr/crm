package com.ss.jcrm.user.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface User {

    long getId();

    @NotNull String getName();

    @NotNull byte[] getPassword();

    @NotNull byte[] getSalt();

    @Nullable Organization getOrganization();

    @NotNull Set<UserRole> getRoles();

    void setRoles(@NotNull Set<UserRole> roles);

    int getVersion();

    void setVersion(int version);
}
