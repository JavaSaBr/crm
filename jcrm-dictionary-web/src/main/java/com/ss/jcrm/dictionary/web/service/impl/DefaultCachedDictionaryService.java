package com.ss.jcrm.dictionary.web.service.impl;

import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

/**
 * @param <T> the type of cached entity.
 * @param <D> the type of DAO.
 * @param <C> the type of collection of all resources.
 * @param <R> the type of entities resource.
 */
public class DefaultCachedDictionaryService<T extends NamedEntity, D extends DictionaryDao<T>, C, R> implements
    CachedDictionaryService<C, R> {

    @AllArgsConstructor
    private static class State<C, R> {

        private final C allResources;
        private final LongDictionary<R> idToResource;
        private final ObjectDictionary<String, R> nameToResource;
    }

    private final DictionaryDao<T> dictionaryDao;
    private final Function<T, R> resourceFunction;
    private final Function<List<T>, C> collectionFunction;

    private volatile State<C, R> state;

    public DefaultCachedDictionaryService(
        @NotNull DictionaryDao<T> dictionaryDao,
        @NotNull Function<T, R> resourceFunction,
        @NotNull Function<List<T>, C> collectionFunction
    ) {
        this.dictionaryDao = dictionaryDao;
        this.resourceFunction = resourceFunction;
        this.collectionFunction = collectionFunction;
    }

    public void reload() {

    }

    @Override
    public @NotNull C getAll() {
        return state.allResources;
    }

    @Override
    public @Nullable R getById(long id) {
        return state.idToResource.get(id);
    }

    @Override
    public @Nullable R getByName(@NotNull String name) {
        return state.nameToResource.get(name);
    }
}
