package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ContactSite {

    @NotNull ContactSite[] EMPTY_ARRAY = new ContactSite[0];

    @NotNull String getUrl();

    void setUrl(@NotNull String url);

    @NotNull SiteType getType();

    void setType(@NotNull SiteType type);
}
