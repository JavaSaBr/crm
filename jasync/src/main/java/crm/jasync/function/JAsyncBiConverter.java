package crm.jasync.function;

import com.github.jasync.sql.db.RowData;
import crm.dao.Dao;
import crm.dao.UniqEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JAsyncBiConverter<D extends Dao<T>, A, T extends UniqEntity> {

    @Nullable T convert(@NotNull D dao, @NotNull A attachment, @NotNull RowData data);
}
