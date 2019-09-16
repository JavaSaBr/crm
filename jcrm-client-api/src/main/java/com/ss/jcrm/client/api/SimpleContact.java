package com.ss.jcrm.client.api;

import com.ss.jcrm.dao.VersionedUniqEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public interface SimpleContact extends VersionedUniqEntity {

    ContactPhoneNumber[] EMPTY_PHONE_NUMBERS = new ContactPhoneNumber[0];
    ContactEmail[] EMPTY_EMAILS = new ContactEmail[0];
    ContactSite[] EMPTY_SITES = new ContactSite[0];
    ContactMessenger[] EMPTY_MESSENGERS = new ContactMessenger[0];

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

    void setBirthday(@NotNull LocalDate birthday);

    @NotNull ContactPhoneNumber[] getPhoneNumbers();

    void setPhoneNumbers(@NotNull ContactPhoneNumber[] phoneNumbers);

    @NotNull ContactEmail[] getEmails();

    void setEmails(@NotNull ContactEmail[] emails);

    @NotNull ContactSite[] getSites();

    void setSites(@NotNull ContactSite[] sites);

    @NotNull ContactMessenger[] getMessengers();

    void setMessengers(@NotNull ContactMessenger[] messengers);

    @Nullable String getCompany();

    void setCompany(@Nullable String company);
}
