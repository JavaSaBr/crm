package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ClientSite {

    @NotNull ClientSite[] EMPTY_ARRAY = new ClientSite[0];

    @NotNull String getUrl();

    void setUrl(@NotNull String url);

    @NotNull SiteType getType();

    void setType(@NotNull SiteType type);
}
