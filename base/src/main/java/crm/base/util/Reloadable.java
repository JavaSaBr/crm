package crm.base.util;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface Reloadable {

  void reload();

  @NotNull Mono<?> reloadAsync();
}
