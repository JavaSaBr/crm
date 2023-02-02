package crm.user.api.dao;

import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Country;
import crm.user.api.Organization;
import com.ss.rlib.common.util.array.Array;
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
