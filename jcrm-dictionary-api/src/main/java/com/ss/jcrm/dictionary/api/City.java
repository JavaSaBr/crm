package com.ss.jcrm.dictionary.api;

import com.ss.jcrm.dao.NamedEntity;
import org.jetbrains.annotations.NotNull;

public interface City extends NamedEntity {

    @NotNull Country getCountry();
}
