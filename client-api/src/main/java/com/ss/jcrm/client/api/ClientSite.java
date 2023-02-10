package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ClientSite {
  @NotNull String url();
  @NotNull SiteType type();
}
