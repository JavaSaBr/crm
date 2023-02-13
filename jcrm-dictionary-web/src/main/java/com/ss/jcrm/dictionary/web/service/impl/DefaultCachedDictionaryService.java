package com.ss.jcrm.dictionary.web.service.impl;

import crm.base.util.Reloadable;
import crm.dao.NamedUniqEntity;
import crm.dictionary.api.dao.DictionaryDao;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @param <T> the type of cached entity.
 * @param <C> the type of collection of all resources.
 * @param <R> the type of entities resource.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultCachedDictionaryService<T extends NamedUniqEntity, R, C> implements CachedDictionaryService<R, C>,
    Reloadable {

  @Value
  @RequiredArgsConstructor
  private static class State<C, R> {

    @NotNull C allResources;
    @NotNull LongDictionary<R> idToResource;
    @NotNull ObjectDictionary<String, R> nameToResource;

    public State(@NotNull Function<List<R>, C> collectionFunction) {
      this.allResources = collectionFunction.apply(List.of());
      this.idToResource = LongDictionary.empty();
      this.nameToResource = ObjectDictionary.empty();
    }
  }

  final @NotNull DictionaryDao<T> dictionaryDao;
  final @NotNull Function<T, R> resourceFunction;
  final @NotNull Function<List<R>, C> collectionFunction;

  volatile @NotNull State<C, R> state;

  public DefaultCachedDictionaryService(
      @NotNull DictionaryDao<T> dictionaryDao,
      @NotNull Function<T, R> resourceFunction,
      @NotNull Function<List<R>, C> collectionFunction) {
    this.state = new State<>(collectionFunction);
    this.dictionaryDao = dictionaryDao;
    this.resourceFunction = resourceFunction;
    this.collectionFunction = collectionFunction;
  }

  @Override
  public void reload() {
    reloadAsync().toFuture().join();
  }

  @Override
  public @NotNull Mono<?> reloadAsync() {
    return dictionaryDao
        .findAll()
        .collectList()
        .doOnNext(this::reload);
  }

  private void reload(@NotNull List<T> entities) {

    var idToResource = DictionaryFactory.<R>newLongDictionary();
    var nameToResource = DictionaryFactory.<String, R>newObjectDictionary();
    var resources = new ArrayList<R>();

    for (var entity : entities) {

      var resource = resourceFunction.apply(entity);

      idToResource.put(entity.id(), resource);
      nameToResource.put(entity.name(), resource);

      resources.add(resource);
    }

    state = new State<>(collectionFunction.apply(resources), idToResource, nameToResource);
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
