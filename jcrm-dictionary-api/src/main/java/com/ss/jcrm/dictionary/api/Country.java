package com.ss.jcrm.dictionary.api;

import com.ss.jcrm.dao.NamedEntity;
import org.jetbrains.annotations.NotNull;

public interface Country extends NamedEntity {

    @NotNull String getFlagCode();
    @NotNull String getPhoneCode();
}
