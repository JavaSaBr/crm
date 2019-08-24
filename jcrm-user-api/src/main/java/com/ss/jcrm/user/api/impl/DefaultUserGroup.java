package com.ss.jcrm.user.api.impl;

import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class DefaultUserGroup implements UserGroup {

    private final long id;

    private String name;
    private Organization organization;
    private Set<AccessRole> roles;

    private volatile int version;

    public DefaultUserGroup(long id, @NotNull String name, @NotNull Organization organization) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.roles = Set.of();
    }
}
