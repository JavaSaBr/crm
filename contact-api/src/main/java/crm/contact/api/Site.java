package crm.contact.api;

import org.jetbrains.annotations.NotNull;

public interface Site {
  @NotNull String url();
  @NotNull SiteType type();
}
