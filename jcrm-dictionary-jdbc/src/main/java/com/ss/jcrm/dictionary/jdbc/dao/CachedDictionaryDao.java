package com.ss.jcrm.dictionary.jdbc.dao;

import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.rlib.common.util.dictionary.DictionaryCollectors;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CachedDictionaryDao<T extends NamedEntity> implements DictionaryDao<T> {

    @Getter
    private static class State<T> {

        private final List<T> objects;
        private final LongDictionary<T> objectById;
        private final Map<String, T> objectByName;

        private State() {
            this.objects = Collections.emptyList();
            this.objectById = LongDictionary.empty();
            this.objectByName = Collections.emptyMap();
        }

        private State(
            @NotNull List<T> objects,
            @NotNull LongDictionary<T> objectById,
            @NotNull Map<String, T> objectByName
        ) {
            this.objects = objects;
            this.objectById = objectById;
            this.objectByName = objectByName;
        }
    }

    private final DictionaryDao<T> dictionaryDao;
    private volatile State state;

    public CachedDictionaryDao(@NotNull DictionaryDao<T> dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
        this.state = new State();
    }

    public void reload() {

        dictionaryDao.findAllAsync()
            .thenAccept(items -> {

                var objects = Collections.unmodifiableList(items);
                var objectById = objects.stream().collect(DictionaryCollectors.toObjectDictionary())
            });
    }

    @Override
    public @NotNull List<T> findAll() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<@NotNull List<T>> findAllAsync() {
        return null;
    }

    @Nullable
    @Override
    public T findById(long id) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<T> findByIdAsync(long id) {
        return null;
    }

    @NotNull
    @Override
    public T requireById(long id) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<T> requireByIdAsync(long id) {
        return null;
    }

    @Nullable
    @Override
    public T findByName(@NotNull String name) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<T> findByNameAsync(@NotNull String name) {
        return null;
    }
}
