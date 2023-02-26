package crm.base.util;

import org.jetbrains.annotations.NotNull;

public interface EnumWithIdAndDescription extends WithId {

  @NotNull String description();
}
