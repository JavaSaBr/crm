package com.ss.jcrm.user.api;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface User {

    long getId();

    @NotNull String getName();

    @NotNull byte[] getPassword();

    @NotNull Set<UserRole> getRoles();

    @NotNull Organization getOrganization();
}
