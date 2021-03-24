package com.ss.jcrm.client.api;

import com.ss.jcrm.dao.VersionedUniqEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

public interface SimpleClient extends VersionedUniqEntity {

    ClientPhoneNumber[] EMPTY_PHONE_NUMBERS = new ClientPhoneNumber[0];
    ClientEmail[] EMPTY_EMAILS = new ClientEmail[0];
    ClientSite[] EMPTY_SITES = new ClientSite[0];
    ClientMessenger[] EMPTY_MESSENGERS = new ClientMessenger[0];

    long getAssignerId();

    void setAssignerId(long assignerId);

    @NotNull long[] getCuratorIds();

    void setCuratorIds(@NotNull long[] curatorIds);

    long getOrganizationId();

    void setOrganizationId(long organizationId);

    @Nullable String getFirstName();

    void setFirstName(@Nullable String firstName);

    @Nullable String getSecondName();

    void setSecondName(@Nullable String secondName);

    @Nullable String getThirdName();

    void setThirdName(@Nullable String thirdName);

    @Nullable LocalDate getBirthday();

    void setBirthday(@Nullable LocalDate birthday);

    @NotNull ClientPhoneNumber[] getPhoneNumbers();

    void setPhoneNumbers(@NotNull ClientPhoneNumber[] phoneNumbers);

    @NotNull ClientEmail[] getEmails();

    void setEmails(@NotNull ClientEmail[] emails);

    @NotNull ClientSite[] getSites();

    void setSites(@NotNull ClientSite[] sites);

    @NotNull ClientMessenger[] getMessengers();

    void setMessengers(@NotNull ClientMessenger[] messengers);

    @Nullable String getCompany();

    void setCompany(@Nullable String company);

    @NotNull Instant getCreated();

    void setCreated(@NotNull Instant created);

    @NotNull Instant getModified();

    void setModified(@NotNull Instant modified);
}
