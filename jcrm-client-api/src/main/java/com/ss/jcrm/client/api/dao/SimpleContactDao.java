package com.ss.jcrm.client.api.dao;

import com.ss.jcrm.client.api.*;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface SimpleContactDao extends Dao<SimpleContact> {

    default @NotNull Mono<@NotNull SimpleContact> create(
        @NotNull User assigner,
        @NotNull Organization organization,
        @NotNull String firstName,
        @NotNull String secondName
    ) {
        return create(
            assigner,
            null,
            organization,
            firstName,
            secondName,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @NotNull Mono<@NotNull SimpleContact> create(
        @NotNull User assigner,
        @Nullable Array<User> curators,
        @NotNull Organization organization,
        @NotNull String firstName,
        @NotNull String secondName,
        @Nullable String thirdName,
        @Nullable LocalDate birthday,
        @Nullable ContactPhoneNumber[] phoneNumbers,
        @Nullable ContactEmail[] emails,
        @Nullable ContactSite[] sites,
        @Nullable ContactMessenger[] messengers,
        @Nullable String company
    );

    /**
     * @throws NotActualObjectDaoException if the contact was changed in another thread/server.
     */
    @NotNull Mono<Void> update(@NotNull SimpleContact contact);

    @NotNull Mono<@NotNull Array<SimpleContact>> findByOrg(long orgId);

    default @NotNull Mono<@NotNull Array<SimpleContact>> findByOrg(@NotNull Organization organization) {
        return findByOrg(organization.getId());
    }

    @NotNull Mono<@NotNull SimpleContact> findByIdAndOrg(long id, long orgId);

    default @NotNull Mono<@NotNull SimpleContact> findByIdAndOrg(long id, @NotNull Organization organization) {
        return findByIdAndOrg(id, organization.getId());
    }

    @NotNull Mono<@NotNull EntityPage<SimpleContact>> findPageByOrg(long offset, long size, long orgId);
}
