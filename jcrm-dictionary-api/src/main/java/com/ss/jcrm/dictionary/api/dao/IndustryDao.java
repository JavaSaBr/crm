package com.ss.jcrm.dictionary.api.dao;

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Industry;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface IndustryDao extends DictionaryDao<Industry> {

    /**
     * @throws DuplicateObjectDaoException if an industry with the same name is exists.
     */
    @NotNull Industry create(@NotNull String name);

    /**
     * @throws CompletionException -> DuplicateObjectDaoException if an industry with the same name is exists.
     */
    @NotNull CompletableFuture<@NotNull Industry> createAsync(@NotNull String name);
}
