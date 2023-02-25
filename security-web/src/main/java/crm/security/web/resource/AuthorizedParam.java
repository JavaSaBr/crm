package crm.security.web.resource;

import crm.user.api.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorizedParam<R> {

  @NotNull R param;
  @NotNull User user;

  public long getOrgId() {
    return user
        .organization()
        .id();
  }
}
