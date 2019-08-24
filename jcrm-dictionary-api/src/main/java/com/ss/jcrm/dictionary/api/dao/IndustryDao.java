package com.ss.jcrm.dictionary.api.dao;

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Industry;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface IndustryDao extends DictionaryDao<Industry> {

    /**
     * @throws DuplicateObjectDaoException if an industry with the same name is exists.
     */
    @NotNull Mono<@NotNull Industry> create(@NotNull String name);
}
