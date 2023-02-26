package crm.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

public interface NamedObjectDao<T extends NamedUniqEntity> extends Dao<T> {

    @NotNull Mono<@Nullable T> findByName(@NotNull String name);
}
