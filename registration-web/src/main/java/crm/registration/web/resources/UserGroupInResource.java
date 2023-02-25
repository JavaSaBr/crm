package crm.registration.web.resources;

import crm.base.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UserGroupInResource(@Nullable String name, long @Nullable [] roles, long id)
    implements RestResource {

  public static @NotNull UserGroupInResource from(@Nullable String name, long @Nullable [] roles) {
    return new UserGroupInResource(name, roles, 0);
  }
}
