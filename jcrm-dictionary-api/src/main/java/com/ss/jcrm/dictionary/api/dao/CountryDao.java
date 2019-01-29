package com.ss.jcrm.dictionary.api.dao;

import com.ss.jcrm.dictionary.api.Country;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface CountryDao extends DictionaryDao<Country> {

    @NotNull Country create(@NotNull String name, @NotNull String flagCode, @NotNull String phoneCode);

    @NotNull CompletableFuture<@NotNull Country> createAsync(
        @NotNull String name,
        @NotNull String flagCode,
        @NotNull String phoneCode
    );
}
