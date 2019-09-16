package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ContactSite {

    @NotNull String getUrl();

    void setUrl(@NotNull String url);

    @NotNull SiteType getType();

    void setType(@NotNull SiteType type);
}
