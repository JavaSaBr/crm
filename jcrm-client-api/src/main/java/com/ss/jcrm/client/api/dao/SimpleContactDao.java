package com.ss.jcrm.client.api.dao;

import com.ss.jcrm.client.api.SimpleContact;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.user.api.Organization;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

public interface SimpleContactDao extends Dao<SimpleContact> {

    @NotNull Mono<@NotNull SimpleContact> create(
        @NotNull Organization organization,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName
    );

    /**
     * @throws NotActualObjectDaoException if the contact was changed in another thread/server.
     */
    @NotNull Mono<Void> update(@NotNull SimpleContact contact);

    @NotNull Mono<@NotNull Array<SimpleContact>> findByOrg(long orgId);

    default @NotNull Mono<@NotNull Array<SimpleContact>> findByOrg(@NotNull Organization organization) {
        return findByOrg(organization.getId());
    }

    @NotNull Mono<@Nullable SimpleContact> findByIdAndOrg(long id, long orgId);

    default @NotNull Mono<@Nullable SimpleContact> findByIdAndOrg(long id, @NotNull Organization organization) {
        return findByIdAndOrg(id, organization.getId());
    }
}
