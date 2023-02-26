package crm.jasync.function;

import crm.dao.UniqEntity;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JAsyncCreationCallback<T extends UniqEntity> {

    @Nullable T handle(long id);
}
