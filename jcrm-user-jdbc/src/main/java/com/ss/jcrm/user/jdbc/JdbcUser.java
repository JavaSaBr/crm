package com.ss.jcrm.user.jdbc;

import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Getter
@EqualsAndHashCode(of = "id")
public class JdbcUser implements User {

    private final String name;
    private final String password;
    private final byte[] salt;
    private final Organization organization;
    private final Set<UserRole> roles;

    private final long id;

    public JdbcUser(@NotNull String name, long id) {
        this.name = name;
        this.id = id;
        this.password = null;
        this.salt = null;
        this.organization = null;
        this.roles = null;
    }

    public JdbcUser(@NotNull User user, @NotNull Set<UserRole> roles) {
        this.name = user.getName();
        this.id = user.getId();
        this.password = user.getPassword();
        this.salt = user.getSalt();
        this.organization = user.getOrganization();
        this.roles = roles;
    }

    public JdbcUser(
        @NotNull String name,
        @NotNull String password,
        @NotNull byte[] salt,
        @Nullable Organization organization,
        @NotNull Set<UserRole> roles,
        long id
    ) {
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.organization = organization;
        this.roles = roles;
        this.id = id;
    }
}
