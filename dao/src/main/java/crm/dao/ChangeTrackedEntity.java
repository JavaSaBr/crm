package crm.dao;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface ChangeTrackedEntity extends Entity {

  @NotNull Instant created();
  @NotNull Instant modified();

  @NotNull ChangeTrackedEntity modified(@NotNull Instant modified);
}
