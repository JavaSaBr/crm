package com.ss.jcrm.dictionary.api.dao;

import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface DictionaryDao<T extends NamedUniqEntity> extends NamedObjectDao<T> {

    @NotNull Mono<@NotNull Array<T>> findAll();

    @NotNull Mono<@NotNull LongDictionary<T>> findAllAsMap();
}
