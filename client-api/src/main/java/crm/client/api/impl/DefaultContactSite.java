package crm.client.api.impl;

import crm.client.api.ClientSite;
import crm.client.api.SiteType;
import org.jetbrains.annotations.NotNull;

public record DefaultContactSite(@NotNull String url, @NotNull SiteType type) implements ClientSite {}
