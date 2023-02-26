package crm.contact.api.impl;

import crm.contact.api.Site;
import crm.contact.api.SiteType;
import org.jetbrains.annotations.NotNull;

public record DefaultSite(@NotNull String url, @NotNull SiteType type) implements Site {}
