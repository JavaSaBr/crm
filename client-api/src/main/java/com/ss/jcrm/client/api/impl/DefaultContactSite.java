package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ClientSite;
import com.ss.jcrm.client.api.SiteType;
import org.jetbrains.annotations.NotNull;

public record DefaultContactSite(@NotNull String url, @NotNull SiteType type) implements ClientSite {}
