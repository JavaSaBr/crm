package com.ss.jcrm.user.api.impl;

import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "email", "organization"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultUser implements User {

    final long id;

    @NotNull final Organization organization;
    @NotNull final Instant created;

    @NotNull String email;

    @Nullable String firstName;
    @Nullable String secondName;
    @Nullable String thirdName;

    @NotNull Instant modified;

    @NotNull byte[] password;
    @NotNull byte[] salt;

    @NotNull Set<AccessRole> roles;
    @NotNull Set<UserGroup> groups;
    @NotNull Set<PhoneNumber> phoneNumbers;
    @NotNull Set<Messenger> messengers;

    volatile int version;
    volatile int passwordVersion;

    boolean emailConfirmed;

    public DefaultUser(
        long id,
        @NotNull Organization organization,
        @NotNull String email,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @NotNull Set<AccessRole> roles,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        @NotNull Set<PhoneNumber> phoneNumbers,
        @NotNull Set<Messenger> messengers,
        @NotNull Instant created,
        @NotNull Instant modified,
        int version,
        int passwordVersion
    ) {
        this.organization = organization;
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.roles = roles;
        this.created = created;
        this.modified = modified;
        this.version = version;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.phoneNumbers = phoneNumbers;
        this.messengers = messengers;
        this.groups = Set.of();
        this.passwordVersion = passwordVersion;
    }
}
