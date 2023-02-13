package crm.dao;

import org.jetbrains.annotations.NotNull;

public interface VersionedUniqEntity extends UniqEntity {
  int version();
  @NotNull VersionedUniqEntity version(int version);
}
