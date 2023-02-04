package com.ss.jcrm.dictionary.api.dao;

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Country;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface CountryDao extends DictionaryDao<Country> {

  /**
   * @throws DuplicateObjectDaoException if a country with the same name is exists.
   */
  @NotNull Mono<@NotNull Country> create(@NotNull String name, @NotNull String flagCode, @NotNull String phoneCode);
}
