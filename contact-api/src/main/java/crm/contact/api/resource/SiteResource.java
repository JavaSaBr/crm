package crm.contact.api.resource;

import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.contact.api.Site;
import crm.contact.api.SiteType;
import crm.contact.api.impl.DefaultSite;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SiteResource(@Nullable String url, long type) {

  public static @NotNull SiteResource of(@NotNull String url, @NotNull SiteType type) {
    return new SiteResource(url, type.id());
  }

  public static @NotNull List<SiteResource> from(@NotNull Collection<Site> sites) {
    return sites
        .stream()
        .map(SiteResource::from)
        .toList();
  }

  public static @NotNull SiteResource from(@NotNull Site site) {
    return new SiteResource(site.url(), site.type().id());
  }

  public static @NotNull Set<Site> toSites(@Nullable List<SiteResource> resources) {
    return resources != null ? resources
        .stream()
        .map(SiteResource::toSite)
        .collect(Collectors.toSet()) : Set.of();
  }

  public @NotNull Site toSite() {
    return new DefaultSite(notNull(url), SiteType.required(type));
  }
}
