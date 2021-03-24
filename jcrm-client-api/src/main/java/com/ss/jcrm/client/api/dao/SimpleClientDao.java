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

public interface SimpleClientDao extends Dao<SimpleClient> {

    default @NotNull Mono<@NotNull SimpleClient> create(
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

    @NotNull Mono<@NotNull SimpleClient> create(
        @NotNull User assigner,
        @Nullable Array<User> curators,
        @NotNull Organization organization,
        @NotNull String firstName,
        @NotNull String secondName,
        @Nullable String thirdName,
        @Nullable LocalDate birthday,
        @Nullable ClientPhoneNumber[] phoneNumbers,
        @Nullable ClientEmail[] emails,
        @Nullable ClientSite[] sites,
        @Nullable ClientMessenger[] messengers,
        @Nullable String company
    );

    /**
     * @throws NotActualObjectDaoException if the contact was changed in another thread/server.
     */
    @NotNull Mono<SimpleClient> update(@NotNull SimpleClient contact);

    @NotNull Mono<@NotNull Array<SimpleClient>> findByOrg(long orgId);

    default @NotNull Mono<@NotNull Array<SimpleClient>> findByOrg(@NotNull Organization organization) {
        return findByOrg(organization.getId());
    }

    @NotNull Mono<@NotNull SimpleClient> findByIdAndOrg(long id, long orgId);

    default @NotNull Mono<@NotNull SimpleClient> findByIdAndOrg(long id, @NotNull Organization organization) {
        return findByIdAndOrg(id, organization.getId());
    }

    @NotNull Mono<@NotNull EntityPage<SimpleClient>> findPageByOrg(long offset, long size, long orgId);
}
