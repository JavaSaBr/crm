package com.ss.jcrm.dictionary.jdbc.dao;

import static com.ss.rlib.common.util.dictionary.DictionaryCollectors.toLongDictionary;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toUnmodifiableMap;
import com.ss.jcrm.dao.Entity;
import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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

    @NotNull
    private volatile State<T> state;

    public CachedDictionaryDao(@NotNull DictionaryDao<T> dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
        this.state = new State<>();
    }

    public void reload() {

        dictionaryDao.findAllAsync()
            .thenAccept(entities -> {

                var objects = Collections.unmodifiableList(entities);
                var objectById = objects.stream()
                    .collect(toLongDictionary(Entity::getId, Function.identity()));
                var objectByName = objects.stream()
                    .collect(toUnmodifiableMap(NamedEntity::getName, Function.identity()));

                state = new State<>(objects, objectById, objectByName);
            });
    }

    @Override
    public @NotNull List<T> findAll() {
        return state.getObjects();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull List<T>> findAllAsync() {
        return completedFuture(state.getObjects());
    }

    @Override
    public @Nullable T findById(long id) {
        return state.getObjectById()
            .get(id);
    }

    @Override
    public @NotNull CompletableFuture<T> findByIdAsync(long id) {
        return completedFuture(findById(id));
    }

    @Override
    public @NotNull T requireById(long id) {
        return ObjectUtils.notNull(findById(id), id,
            value -> new ObjectNotFoundDaoException("Can't find an entity with the id " + value));
    }

    @Override
    public @NotNull CompletableFuture<T> requireByIdAsync(long id) {

        T entity = findById(id);

        if (entity != null) {
            return completedFuture(entity);
        }

        return CompletableFuture.failedFuture(
            new ObjectNotFoundDaoException("Can't find an entity with the id " + id)
        );
    }

    @Override
    public @Nullable T findByName(@NotNull String name) {
        return state.getObjectByName()
            .get(name);
    }

    @Override
    public @NotNull CompletableFuture<T> findByNameAsync(@NotNull String name) {
        return completedFuture(findByName(name));
    }
}
