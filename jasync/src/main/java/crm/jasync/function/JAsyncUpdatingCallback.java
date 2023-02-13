package crm.jasync.function;

import crm.dao.Dao;
import crm.dao.UniqEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JAsyncUpdatingCallback<D extends Dao<T>, T extends UniqEntity> {

    @Nullable T handle(@NotNull D dao, @NotNull T entity);
}
