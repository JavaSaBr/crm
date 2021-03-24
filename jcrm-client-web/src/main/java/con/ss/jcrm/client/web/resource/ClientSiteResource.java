package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ss.jcrm.client.api.ClientSite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientSiteResource(
    @Nullable String url,
    @Nullable String type
) {

    public static @NotNull ClientSiteResource from(@NotNull ClientSite site) {
        return new ClientSiteResource(site.getUrl(), site.getType().name());
    }
}
