package com.ss.jcrm.dictionary.api.dao;

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface CityDao extends DictionaryDao<City> {

    /**
     * @throws DuplicateObjectDaoException if a city with the same name is exists.
     */
    @NotNull Mono<@NotNull City> create(@NotNull String name, @NotNull Country country);
}
