package com.ss.jcrm.client.api;

import com.ss.jcrm.dao.VersionedEntity;
import org.jetbrains.annotations.Nullable;

public interface SimpleContact extends VersionedEntity {

    long getOrganizationId();

    void setOrganizationId(long organizationId);

    @Nullable String getFirstName();

    void setFirstName(@Nullable String firstName);

    @Nullable String getSecondName();

    void setSecondName(@Nullable String secondName);

    @Nullable String getThirdName();

    void setThirdName(@Nullable String thirdName);
}
