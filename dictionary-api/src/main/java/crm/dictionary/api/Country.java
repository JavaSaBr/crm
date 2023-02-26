package crm.dictionary.api;

import crm.dao.NamedUniqEntity;
import org.jetbrains.annotations.NotNull;

public interface Country extends NamedUniqEntity {
    @NotNull String flagCode();
    @NotNull String phoneCode();
}
