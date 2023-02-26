package crm.dictionary.api.dao;

import crm.dao.exception.DuplicateObjectDaoException;
import crm.dictionary.api.City;
import crm.dictionary.api.Country;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface CityDao extends DictionaryDao<City> {

    /**
     * @throws DuplicateObjectDaoException if a city with the same name is exists.
     */
    @NotNull Mono<City> create(@NotNull String name, @NotNull Country country);
}
