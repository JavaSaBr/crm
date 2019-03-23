package com.ss.jcrm.dictionary.api.dao;

import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DictionaryDao<T extends NamedEntity> extends NamedObjectDao<T> {

    @NotNull List<T> findAll();

    @NotNull LongDictionary<T> findAllAsMap();

    @NotNull CompletableFuture<@NotNull List<T>> findAllAsync();

    @NotNull CompletableFuture<@NotNull LongDictionary<T>> findAllAsMapAsync();
}
