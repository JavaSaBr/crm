package com.ss.jcrm.user.impl.jdbc;

import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserRole;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class JdbcUser implements User {

    private final String name;
    private final byte[] password;
    private final Organization organization;

    private final long id;

    @Nullable
    private volatile Set<UserRole> roles;

    public JdbcUser(@NotNull String name, long id) {
        this.name = name;
        this.id = id;
        this.password = null;
        this.organization = null;
    }

    public JdbcUser(@NotNull String name, @NotNull byte[] password, @NotNull Organization organization, long id) {
        this.name = name;
        this.password = password;
        this.organization = organization;
        this.id = id;
    }
}
