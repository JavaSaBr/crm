package crm.dictionary.api;

import crm.dao.NamedUniqEntity;
import org.jetbrains.annotations.NotNull;

public interface City extends NamedUniqEntity {
    @NotNull Country country();
}
