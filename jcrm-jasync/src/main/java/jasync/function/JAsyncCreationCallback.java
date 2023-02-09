package jasync.function;

import com.ss.jcrm.dao.UniqEntity;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JAsyncCreationCallback<T extends UniqEntity> {

    @Nullable T handle(long id);
}
