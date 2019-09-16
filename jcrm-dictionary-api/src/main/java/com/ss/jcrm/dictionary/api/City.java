package com.ss.jcrm.dictionary.api;

import com.ss.jcrm.dao.NamedUniqEntity;
import org.jetbrains.annotations.NotNull;

public interface City extends NamedUniqEntity {

    @NotNull Country getCountry();
}
