package com.ss.jcrm.user.api.impl;

import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = {"id", "name", "organization"})
public class DefaultUserGroup implements UserGroup {

    final long id;
    @NotNull final Instant created;
    @NotNull final Organization organization;

    volatile @NotNull String name;
    volatile @NotNull Set<AccessRole> roles;
    volatile @NotNull Instant modified;

    private volatile int version;

    public DefaultUserGroup(
        long id,
        @NotNull String name,
        @NotNull Set<AccessRole> roles,
        @NotNull Organization organization,
        @NotNull Instant created,
        @NotNull Instant modified,
        int version
    ) {
        this.id = id;
        this.created = created;
        this.organization = organization;
        this.name = name;
        this.roles = roles;
        this.modified = modified;
        this.version = version;
    }
}
