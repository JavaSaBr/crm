package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.client.api.ClientSite;
import crm.client.api.SiteType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientSiteResource(@Nullable String url, @Nullable String type) {

  public static @NotNull ClientSiteResource of(@NotNull String url, @NotNull SiteType type) {
    return new ClientSiteResource(url, type.description());
  }

  public static @NotNull ClientSiteResource from(@NotNull ClientSite site) {
    return new ClientSiteResource(
        site.url(),
        site.type().description());
  }
}
