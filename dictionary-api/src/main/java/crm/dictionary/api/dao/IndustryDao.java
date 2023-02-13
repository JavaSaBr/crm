package crm.dictionary.api.dao;

import crm.dao.exception.DuplicateObjectDaoException;
import crm.dictionary.api.Industry;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface IndustryDao extends DictionaryDao<Industry> {

    /**
     * @throws DuplicateObjectDaoException if an industry with the same name is exists.
     */
    @NotNull Mono<Industry> create(@NotNull String name);
}
