package com.ss.jcrm.user.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface User {

    long getId();

    @NotNull String getName();

    @NotNull String getPassword();

    @NotNull byte[] getSalt();

    @NotNull Set<UserRole> getRoles();

    @Nullable Organization getOrganization();
}
