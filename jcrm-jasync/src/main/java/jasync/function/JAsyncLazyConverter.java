package jasync.function;

import com.github.jasync.sql.db.RowData;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.UniqEntity;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface JAsyncLazyConverter<D extends Dao<T>, T extends UniqEntity> {

    @NotNull Mono<T> convert(@NotNull D dao, @NotNull RowData data);
}