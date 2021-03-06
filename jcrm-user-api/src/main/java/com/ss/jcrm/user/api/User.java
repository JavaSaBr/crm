package com.ss.jcrm.user.api;

import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public interface User extends VersionedUniqEntity {

    @NotNull String getEmail();

    void setEmail(@NotNull String email);

    @NotNull Organization getOrganization();

    @Nullable String getFirstName();

    void setFirstName(@Nullable String firstName);

    @Nullable String getSecondName();

    void setSecondName(@Nullable String secondName);

    @Nullable String getThirdName();

    void setThirdName(@Nullable String thirdName);

    @Nullable LocalDate getBirthday();

    void setBirthday(@Nullable LocalDate birthday);

    @NotNull byte[] getPassword();

    void setPassword(@NotNull byte[] password);

    @NotNull byte[] getSalt();

    void setSalt(@NotNull byte[] salt);

    @NotNull Set<AccessRole> getRoles();

    default @NotNull Stream<AccessRole> streamRoles() {
        return getRoles().stream();
    }

    void setRoles(@NotNull Set<AccessRole> roles);

    @NotNull Set<UserGroup> getGroups();

    default @NotNull Stream<UserGroup> streamGroups() {
        return getGroups().stream();
    }

    void setGroups(@NotNull Set<UserGroup> groups);

    default void addGroup(@NotNull UserGroup userGroup) {

        var newGroups = new HashSet<>(getGroups());
        newGroups.add(userGroup);

        setGroups(Set.copyOf(newGroups));
    }

    @NotNull Set<PhoneNumber> getPhoneNumbers();

    void setPhoneNumbers(@NotNull Set<PhoneNumber> phoneNumbers);

    @NotNull Set<Messenger> getMessengers();

    void setMessengers(@NotNull Set<Messenger> messengers);

    boolean isEmailConfirmed();

    void setEmailConfirmed(boolean confirmed);

    int getPasswordVersion();

    void setPasswordVersion(int passwordVersion);

    @NotNull Instant getCreated();

    @NotNull Instant getModified();

    void setModified(@NotNull Instant modified);
}
