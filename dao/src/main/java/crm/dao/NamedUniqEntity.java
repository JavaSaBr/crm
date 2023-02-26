package crm.dao;

import org.jetbrains.annotations.NotNull;

public interface NamedUniqEntity extends UniqEntity {
  @NotNull String name();
}
