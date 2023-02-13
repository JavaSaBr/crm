package crm.contact.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ss.jcrm.dao.Entity;
import crm.contact.api.impl.DefaultEmail;
import org.jetbrains.annotations.NotNull;

public interface Email extends Entity {
  @NotNull String email();
  @NotNull EmailType type();

  @JsonCreator
  static @NotNull Email of(@NotNull String email, @NotNull EmailType type) {
    return new DefaultEmail(email, type);
  }
}
