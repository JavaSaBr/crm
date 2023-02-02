package crm.user.api;

import org.jetbrains.annotations.NotNull;

public interface WithOrganization {
  @NotNull Organization organization();
}
