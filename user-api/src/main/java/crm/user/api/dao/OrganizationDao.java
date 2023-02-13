package crm.user.api.dao;

import crm.dao.NamedObjectDao;
import crm.dao.exception.DuplicateObjectDaoException;
import crm.dictionary.api.Country;
import crm.user.api.Organization;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrganizationDao extends NamedObjectDao<Organization> {

    /**
     * @throws DuplicateObjectDaoException if an organization with the same name is already exist.
     */
    @NotNull Mono<Organization> create(@NotNull String name, @NotNull Country country);

    @NotNull Flux<Organization> findAll();

    @NotNull Mono<Boolean> existByName(@NotNull String name);

    @NotNull Mono<Boolean> deleteById(long id);
}
