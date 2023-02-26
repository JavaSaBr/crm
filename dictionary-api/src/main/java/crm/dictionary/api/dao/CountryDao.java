package crm.dictionary.api.dao;

import crm.dao.exception.DuplicateObjectDaoException;
import crm.dictionary.api.Country;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface CountryDao extends DictionaryDao<Country> {

  /**
   * @throws DuplicateObjectDaoException if a country with the same name is exists.
   */
  @NotNull Mono<Country> create(@NotNull String name, @NotNull String flagCode, @NotNull String phoneCode);
}
