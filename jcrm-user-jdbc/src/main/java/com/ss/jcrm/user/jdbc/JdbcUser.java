package com.ss.jcrm.user.jdbc;

import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserGroup;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name", "organization"})
public class JdbcUser implements User {

    private final long id;
    private final Organization organization;

    private String name;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String phoneNumber;

    private byte[] password;
    private byte[] salt;

    private Set<AccessRole> roles;
    private Set<UserGroup> groups;

    private volatile int version;

    public JdbcUser(
        long id,
        @NotNull Organization organization,
        @NotNull String name,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @NotNull Set<AccessRole> roles,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        @Nullable String phoneNumber,
        int version
    ) {
        this.organization = organization;
        this.id = id;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.roles = roles;
        this.version = version;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.phoneNumber = phoneNumber;
        this.groups = Set.of();
    }
}
