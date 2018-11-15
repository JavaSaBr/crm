package com.ss.jcrm.user.jdbc;

import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name", "organization"})
public class JdbcUser implements User {

    private final String name;
    private final String password;
    private final byte[] salt;
    private final Organization organization;

    private final long id;

    @Setter
    private volatile Set<UserRole> roles;

    @Setter
    private volatile int version;

    public JdbcUser(@NotNull String name, long id, int version) {
        this.name = name;
        this.id = id;
        this.version = version;
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
        this.version = user.getVersion();
    }

    public JdbcUser(
        @NotNull String name,
        @NotNull String password,
        @NotNull byte[] salt,
        @Nullable Organization organization,
        @NotNull Set<UserRole> roles,
        long id,
        int version
    ) {
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.organization = organization;
        this.roles = roles;
        this.id = id;
        this.version = version;
    }
}
