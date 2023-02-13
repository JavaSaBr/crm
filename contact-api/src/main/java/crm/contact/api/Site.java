package crm.contact.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import crm.contact.api.impl.DefaultSite;
import org.jetbrains.annotations.NotNull;

public interface Site {
  @NotNull String url();
  @NotNull SiteType type();

  @JsonCreator
  static @NotNull Site of(@NotNull String url, @NotNull SiteType type) {
    return new DefaultSite(url, type);
  }
}
