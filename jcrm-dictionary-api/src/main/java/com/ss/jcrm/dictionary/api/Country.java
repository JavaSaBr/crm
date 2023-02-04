package com.ss.jcrm.dictionary.api;

import com.ss.jcrm.dao.NamedUniqEntity;
import org.jetbrains.annotations.NotNull;

public interface Country extends NamedUniqEntity {
    @NotNull String flagCode();
    @NotNull String phoneCode();
}
