package crm.registration.web.resources;

import crm.user.api.User;
import crm.base.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;

public record AuthenticationOutResource(@NotNull String token, @NotNull UserOutResource user)
    implements RestResource {

  public static @NotNull AuthenticationOutResource from(@NotNull String token, @NotNull User user) {
    return new AuthenticationOutResource(token, UserOutResource.from(user));
  }
}
