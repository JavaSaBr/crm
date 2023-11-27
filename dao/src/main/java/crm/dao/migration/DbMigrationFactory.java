package crm.dao.migration;

import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DbMigrationFactory<T> {

  @Getter
  @NotNull Class<T> type;
  @NotNull Supplier<T> factory;

  public @NotNull T build() {
    return factory.get();
  }
}
