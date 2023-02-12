package crm.contact.api;

import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;

public interface Email extends Entity {
  @NotNull String email();
  @NotNull EmailType type();
}
